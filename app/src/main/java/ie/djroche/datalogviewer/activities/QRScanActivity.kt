package ie.djroche.datalogviewer.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import ie.djroche.datalogviewer.databinding.ActivityQrscanBinding
import ie.djroche.datalogviewer.R
import android.view.SurfaceHolder
import java.io.IOException
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.material.snackbar.Snackbar
import ie.djroche.datalogviewer.main.MainApp

class QRScanActivity : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private lateinit var binding: ActivityQrscanBinding
    lateinit var app: MainApp
//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrscanBinding.inflate(layoutInflater)

        //set toolbar contents
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        val view = binding.root
        setContentView(view)

    // reference to main app
        app = application as MainApp

    // ask for permission to use Camera
        if (ContextCompat.checkSelfPermission(
                this@QRScanActivity, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }
        val aniSlide: Animation =
            AnimationUtils.loadAnimation(this@QRScanActivity, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)
        }

    // ------------------   Load the Menu Items  --------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scanqr, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // -------------------   Process the Click ----------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_Back -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //--------------------------------------------------------------------------------------------------
    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this@QRScanActivity,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }
    //--------------------------------------------------------------------------------------------------
    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
           @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    //Start preview after 1s delay
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Snackbar.make(binding.root,getString(R.string.scanner_has_been_closed), Snackbar.LENGTH_LONG)
                    .show()
            }

            override fun receiveDetections(detections: Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue
                    app.qrCode = scannedValue
                    runOnUiThread {
                        cameraSource.stop()
                        Snackbar.make(binding.root,getString(R.string.scanner_has_been_closed) + " $scannedValue", Snackbar.LENGTH_LONG)
                            .show()
                        setResult(RESULT_OK);
                        finish()
                    }
                }
            }
        })
    }
    //--------------------------------------------------------------------------------------------------
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Snackbar.make(binding.root,getString(R.string.Permission_Denied) , Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    //--------------------------------------------------------------------------------------------------
    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }

}