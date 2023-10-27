package ie.djroche.datalogviewer.activites

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.adaptors.SiteRVAdaptor
import ie.djroche.datalogviewer.adaptors.SiteListener
import ie.djroche.datalogviewer.databinding.ActivityMainBinding
import ie.djroche.datalogviewer.helpers.cancelRequests
import ie.djroche.datalogviewer.helpers.sendReadQRDataRequest

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
            // General Help function  for testing
            R.id.item_F1->{
                //sendTestRequest(app)
                sendReadQRDataRequest(app,"QR-000006-000003")
            }
            //scan QR code
            R.id.item_ScanQR -> {
                showScanQR()
            }
            // show all items in grid
            R.id.item_Grid ->{
                showGrid()
            }
            //load settings
            R.id.item_settings->{
                showSettings()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        cancelRequests(app) // cancel all outstanding HTTP requests
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
    private fun showSettings() {
        Timber.i("DataLogViewer Settings selected")
        app.qrCode = "-"
        //launch the KPI page
        val launcherIntent = Intent(this, SettingsActivity::class.java)
        getResultSettings.launch(launcherIntent)
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
    private val getResultSettings =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            app.preferences = PreferenceManager.getDefaultSharedPreferences(this)
        }
} // ------------------------------END Of Class -----

