package ie.djroche.datalogviewer.activites

//ToDo:Add QRScanActivity

//ToDo:Add Hamburger Menu



import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.databinding.ActivityMainBinding
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteDataModel
import ie.djroche.datalogviewer.models.SiteModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
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
                loadDummyData()
            }
        }
        return super.onOptionsItemSelected(item)
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

    //-------------------------------------------------------------------------------------------------------
    private fun loadDummyData(){
        var  myNewSite : SiteModel = SiteModel(data=mutableListOf<SiteDataModel>())
        var  myNewSite1 : SiteModel = SiteModel(data=mutableListOf<SiteDataModel>())
        var id =0L
        // create the site data for 1st site
        myNewSite.description = "Test Site 001"
        myNewSite.data.add(SiteDataModel(1,"Temperature",R.drawable.temp,23.4,"Deg C").copy())
        myNewSite.data.add(SiteDataModel(1,"Temperature",R.drawable.temp,23.4,"Deg C").copy())
        myNewSite.data.add(SiteDataModel(2,"Motor RPM",R.drawable.speedometer,800.0,"RPM").copy())
        myNewSite.data.add(SiteDataModel(3,"Valve Status",R.drawable.valve,0.0,"OFF").copy())
        myNewSite.data.add(SiteDataModel(4,"Flow Rate",R.drawable.flow,125.0,"L/m").copy())
        myNewSite.data.add(SiteDataModel(5,"Tank Level",R.drawable.tank,65.0,"%").copy())
        myNewSite.qrcode = "QR-000006-000001"
        id = app.sites.create(myNewSite.copy())
        // create the site data for 2nd site
        myNewSite1.description = "Test Site 002"
        myNewSite1.data.add(SiteDataModel(3,"Temperature",R.drawable.temp,13.4,"Deg C").copy())
        myNewSite1.data.add(SiteDataModel(4,"Motor RPM",R.drawable.speedometer,100.0,"RPM").copy())
        myNewSite1.qrcode = "QR-000006-000002"
        id = app.sites.create(myNewSite1.copy())

    }

} // ------------------------------END Of Class -----

