package ie.djroche.datalogviewer.activities
//------------------------------------------------------------------------------------------------
import android.app.Activity
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

import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel
import timber.log.Timber
//------------------------------------------------------------------------------------------------
class MainActivity : AppCompatActivity(), SiteListener  {
    private lateinit var binding: ActivityMainBinding
    lateinit var app: MainApp
    // -------------------------------------------------------------------------------------------
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
        if (app.userLoggedIn == true ){
            ShowSites()
        }
        Timber.i("Main Activity started...")
    }
    // ------------------   Load the Menu Items  -------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        UpdateMainMenuIcons()
        return super.onCreateOptionsMenu(menu)
    }
    // ------------------   Process the Menu Items events  ---------------------------------------
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
            // Log in
            R.id.item_LogIn->{
                showLogin()
            }

            // Log out
            R.id.item_LogOut->{
                LogOut()
            }
            //new Site
            R.id.item_AddSite->{
                AddSite()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    // -------------------------------------------------------------------------------------------
    override fun onStop() {
        super.onStop()
        cancelRequests(app) // cancel all outstanding HTTP requests
    }
    // --------------------------------------------------------------------------------------------
    override fun onBackPressed() {
        // No Pressing Back allowed
    }
    // --------------------------------------------------------------------------------------------
    private fun showLogin() {
        Timber.i("Show Login selected")
        //launch the KPI page
        val launcherIntent = Intent(this, LoginActivity::class.java)
        getResultLogin.launch(launcherIntent)
    }
    // --------------------------------------------------------------------------------------------
    private fun LogOut() {
        Timber.i("LogOut selected")
        app.userLoggedIn =false
        HideSites()
        UpdateMainMenuIcons()
    }
    // --------------------------------------------------------------------------------------------
    private fun showGrid() {
        Timber.i("Show grid selected")
        try{
            //launch the KPI page
            val launcherIntent = Intent(this, KpiListActivity::class.java)
            getResult.launch(launcherIntent)
        }catch (e: Exception) {
            Timber.i("showGrid Error " + e.message)
        }
    }
    // --------------------------------------------------------------------------------------------
    private fun showScanQR() {
        Timber.i("DataLogViewer Scan QR selected")
        app.qrCode = "-"
        //launch the KPI page
        val launcherIntent = Intent(this, QRScanActivity::class.java)
        getResultQRScan.launch(launcherIntent)
    }
    // --------------------------------------------------------------------------------------------
    private fun showSettings() {
        Timber.i("DataLogViewer Settings selected")
        app.qrCode = "-"
        //launch the KPI page
        val launcherIntent = Intent(this, SettingsActivity::class.java)
        getResultSettings.launch(launcherIntent)
    }
    // --------------------------------------------------------------------------------------------
    override fun onSiteClick(site: SiteModel) {
        val launcherIntent = Intent(this, MainActivity::class.java)
        getClickResult.launch(launcherIntent)
        app.qrCode =site.qrcode
        showGrid()
    }
    // --------------------------------------------------------------------------------------------
    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0,app.sites.findAllForUser(app.user.id.toString()).size)
               // notifyItemRangeChanged(0,app.sites.findAll().size)
            }
        }
    // --------------------------------------------------------------------------------------------
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Timber.i("Grid View Returned OK")
            }
        }

    // --------------------------------------------------------------------------------------------
    private val getResultQRScan =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Timber.i("QR Scan Returned OK")
                //ToDo: Validate the QR Code
                if  (app.qrCode != "-"){
                    showGrid()
                }
            }
        }
    // --------------------------------------------------------------------------------------------
    private val getResultSettings =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // set the preferences
            app.preferences = PreferenceManager.getDefaultSharedPreferences(this)
        }
    // --------------------------------------------------------------------------------------------
    private val getResultLogin =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Timber.i("Login OK")
                app.userLoggedIn = true
                UpdateMainMenuIcons()
                ShowSites()
            }
        }

    // --------------------------------------------------------------------------------------------
    private fun ShowSites(){
        //
        //binding.recyclerView.adapter = SiteRVAdaptor(app.sites.findAll(),this)
        binding.recyclerView.adapter = SiteRVAdaptor(app.sites.findAllForUser(app.user.id.toString()),this)
    }
    // --------------------------------------------------------------------------------------------
    private fun HideSites(){
        // hide sites
        binding.recyclerView.adapter =SiteRVAdaptor(emptyList(),this)
    }
    // --------------------------------------------------------------------------------------------
    private fun AddSite(){
        var myNewSite: SiteModel = SiteModel(data = mutableListOf<SiteKPIModel>())
        myNewSite.description= "New Site"
        myNewSite.userid = app.user.id.toString()
        myNewSite.data.add(SiteKPIModel())
        app.sites.create(myNewSite.copy())
        ShowSites()
    }
    //---------------------------------------------------------------------------------------------
    // show different menu icons depending on user logedd in status
    private fun UpdateMainMenuIcons(){
        if (app.userLoggedIn != true){
            binding.toolbar.menu.getItem(0).isVisible =false// F1 test
            binding.toolbar.menu.getItem(1).isVisible =false// F2 Settings
            binding.toolbar.menu.getItem(2).isVisible =false// F3 Test grid
            binding.toolbar.menu.getItem(3).isVisible =false// F4 QR scan
            binding.toolbar.menu.getItem(4).isVisible =true// F5 logion
            binding.toolbar.menu.getItem(5).isVisible =false // F6 logout
            binding.toolbar.menu.getItem(6).isVisible =false // F7 add site
        } else {
            binding.toolbar.menu.getItem(0).isVisible =false// F1 test
            binding.toolbar.menu.getItem(1).isVisible =true// F2 Settings
            binding.toolbar.menu.getItem(2).isVisible =false// F3 Test grid
            binding.toolbar.menu.getItem(3).isVisible =true// F4 QR scan
            binding.toolbar.menu.getItem(4).isVisible =false// F5 logion
            binding.toolbar.menu.getItem(5).isVisible =true // F6 logout
            binding.toolbar.menu.getItem(6).isVisible =true // F7 add site
        }
    }
} // ------------------------------END Of Class ---------------------------------------------------

