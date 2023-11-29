package ie.djroche.datalogviewer.activities
//------------------------------------------------------------------------------------------------
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.adaptors.SiteRVAdaptor
import ie.djroche.datalogviewer.adaptors.SiteListener
import ie.djroche.datalogviewer.databinding.ActivityMainBinding
import ie.djroche.datalogviewer.databinding.NavHeaderBinding
import ie.djroche.datalogviewer.helpers.cancelRequests
import ie.djroche.datalogviewer.helpers.sendReadQRDataRequest

import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel
import timber.log.Timber
//------------------------------------------------------------------------------------------------
class MainActivity : AppCompatActivity(), SiteListener ,
    NavigationView.OnNavigationItemSelectedListener  {

    lateinit var app: MainApp
    private lateinit var acMainBinding: ActivityMainBinding
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    lateinit var navViewHeaderBinding: NavHeaderBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    // -------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        acMainBinding = ActivityMainBinding.inflate(layoutInflater)
        //set toolbar contents
        acMainBinding.toolbar.title = title
        setSupportActionBar(acMainBinding.toolbar)

        // bind the activity View
        setContentView(acMainBinding.root)

        //launch the main app
        app = application as MainApp

        // launch and bind the recycler view
        val layoutManager = LinearLayoutManager(this)
        acMainBinding.recyclerView.layoutManager = layoutManager
        if (app.userLoggedIn == true ){
            ShowSites()
        }

        // REf: https://www.geeksforgeeks.org/navigation-drawer-in-android/
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //set nav view  and listener
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        // get binding for the header view Ref: https://stackoverflow.com/questions/33962548/how-to-data-bind-to-a-header
        val viewHeader = acMainBinding.navView.getHeaderView(0)
        navViewHeaderBinding = viewHeader.let { NavHeaderBinding.bind(it) }

        // add binding for Floating Action Button
        acMainBinding.fab.setOnClickListener {
            Timber.i("FAB Clicked")
            showScanQR()
        }

        Timber.i("Main Activity started...")
    }
    // ------------------   Load the Menu Items  -------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        UpdateMainMenuIcons()
        return super.onCreateOptionsMenu(menu)
    }
    // -------------------- Update Nav   Drawer header Text --------------------------------------
    private fun UpdateNavHeader(){
        // Update the data on the nave drawer header
        val userText : String = getString(R.string.user_name) + app.user.firstName + " "+ app.user.lastName
        navViewHeaderBinding.tvUser.text = userText
        navViewHeaderBinding.tvTitle.text =getString(R.string.app_name)
    }
    // ------------------   Process the Menu Items events  ---------------------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            UpdateNavHeader()
            true
        } else {
            when (item.itemId) {
                // General Help function  for testing
                R.id.item_F1 -> {
                    //sendTestRequest(app)
                    sendReadQRDataRequest(app, "QR-000006-000003")
                }
                //scan QR code
                R.id.item_ScanQR -> {
                    showScanQR()
                }
                // show all items in grid
                R.id.item_Grid -> {
                    showGrid()
                }
                //load settings
                R.id.item_settings -> {
                    showSettings()
                }
                // Log in
                R.id.item_LogIn -> {
                    showLogin()
                }

                // Log out
                R.id.item_LogOut -> {
                    LogOut()
                }
                //new Site
                R.id.item_AddSite -> {
                    AddSite()
                }
            }
            return super.onOptionsItemSelected(item)
        }
    }
    // -------------------------------------------------------------------------------------------
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                Timber.i( "nav_settings clicked")
                showSettings()
            }
            R.id.nav_logout -> {
                Timber.i( "nav_logout clicked")
                LogOut()
            }
            R.id.nav_login -> {
                Timber.i("nav_login clicked")
                showLogin()
            }
            R.id.nav_addSite ->{
                Timber.i("nav_addSite clicked")
                AddSite()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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
    //---------------------------------------------------------------------------------------------
    override fun onRestart() {
        val userText : String = getString(R.string.user_name) + app.user.firstName + " "+ app.user.lastName
        navViewHeaderBinding.tvUser.text = userText
        navViewHeaderBinding.tvTitle.text =getString(R.string.app_name)
        super.onRestart()
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
        // test nav -UpdateMainMenuIcons()
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
               (acMainBinding.recyclerView.adapter)?.notifyItemRangeChanged(0,app.sites.findAllForUser(app.user.id.toString()).size)
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
                    // check if scanned site is found or if it is found for that user
                    var scannedSite = app.sites.findByQR(app.qrCode)!!

                    if (scannedSite.userid == app.user.id){
                        showGrid()
                    } else {
                        Snackbar.make( acMainBinding.root,R.string.site_not_valid_or_different_user, Snackbar.LENGTH_LONG)
                            .show()
                    }

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
        acMainBinding.recyclerView.adapter = SiteRVAdaptor(app.sites.findAllForUser(app.user.id.toString()),this)
    }
    // --------------------------------------------------------------------------------------------
    private fun HideSites(){
        // hide sites
       acMainBinding.recyclerView.adapter =SiteRVAdaptor(emptyList(),this)
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
    // test nav -
   private fun UpdateMainMenuIcons(){
        if (app.userLoggedIn != true){
            acMainBinding.toolbar.menu.getItem(0).isVisible =false// F1 test
            acMainBinding.toolbar.menu.getItem(1).isVisible =false// F2 Settings
            acMainBinding.toolbar.menu.getItem(2).isVisible =false// F3 Test grid
            acMainBinding.toolbar.menu.getItem(3).isVisible =false// F4 QR scan
            acMainBinding.toolbar.menu.getItem(4).isVisible =true// F5 logion
            acMainBinding.toolbar.menu.getItem(5).isVisible =false // F6 logout
            acMainBinding.toolbar.menu.getItem(6).isVisible =false // F7 add site
        } else {
            acMainBinding.toolbar.menu.getItem(0).isVisible =false// F1 test
            acMainBinding.toolbar.menu.getItem(1).isVisible =true// F2 Settings
            acMainBinding.toolbar.menu.getItem(2).isVisible =false// F3 Test grid
            acMainBinding.toolbar.menu.getItem(3).isVisible =true// F4 QR scan
            acMainBinding.toolbar.menu.getItem(4).isVisible =false// F5 logion
            acMainBinding.toolbar.menu.getItem(5).isVisible =true // F6 logout
            acMainBinding.toolbar.menu.getItem(6).isVisible =true // F7 add site
        }
    }


} // ------------------------------END Of Class ---------------------------------------------------

