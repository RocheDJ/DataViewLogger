package ie.djroche.datalogviewer.activites

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.adaptors.SiteRVAdaptor
import ie.djroche.datalogviewer.adaptors.SiteListener
import ie.djroche.datalogviewer.databinding.ActivityMainBinding
import ie.djroche.datalogviewer.helpers.cancelRequests
import ie.djroche.datalogviewer.helpers.loadDummySiteData
import ie.djroche.datalogviewer.helpers.loadDummyUserData
import ie.djroche.datalogviewer.helpers.sendTestRequest
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteModel
import timber.log.Timber

class MainActivity : AppCompatActivity(), SiteListener  {
    private lateinit var binding: ActivityMainBinding
    lateinit var app: MainApp

    // -----------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        //set toolbar contents
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        // bind the activity View
        setContentView(binding.root)

        //launch the main app
        app = application as MainApp

        // launch and bind the recycler view
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = SiteRVAdaptor(app.sites.findAll(),this)

        Timber.i("Main Activity started...")
    }
    // ------------------   Load the Menu Items  --------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    // ------------------   Process the Menu Items events  --------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_ScanQR -> {
                showScanQR()
            }
            R.id.item_Grid ->{
                showGrid()
            }
            R.id.item_F1->{
                // populate the models with dummy data for now
                //loadDummyUserData(app)
              //  loadDummySiteData(app)
                sendTestRequest(app)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        cancelRequests(app) // cancel all outstanding HTTP requests
    }

    // -----------------------------------------------------------------------------------------------------
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Timber.i("Grid View Returned OK")
            }
        }

    // -----------------------------------------------------------------------------------------------------
    private val getResultQRScan =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Timber.i("QR Scan Returned OK")
                if  (app.qrCode != "-"){
                    showGrid()
                }
            }
        }
    // -----------------------------------------------------------------------------------------------------
    private fun showGrid() {
        Timber.i("Show grid selected")
        //launch the KPI page
        val launcherIntent = Intent(this, KpiListActivity::class.java)
        getResult.launch(launcherIntent)
    }
    // -----------------------------------------------------------------------------------------------------
    private fun showScanQR() {
        Timber.i("DataLogViewer Scan QR selected")
        app.qrCode = "-"
        //launch the KPI page
        val launcherIntent = Intent(this, QRScanActivity::class.java)
        getResultQRScan.launch(launcherIntent)
        //ToDo: Validate the QR Code
    }
    // -----------------------------------------------------------------------------------------------------
    override fun onSiteClick(site: SiteModel) {
        val launcherIntent = Intent(this, MainActivity::class.java)
        getClickResult.launch(launcherIntent)
        app.qrCode =site.qrcode
        showGrid()
    }
    // -----------------------------------------------------------------------------------------------------
    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.sites.findAll().size)
            }
        }

} // ------------------------------END Of Class -----

