package ie.djroche.datalogviewer.activites
//ToDo: Add GridActivity

//ToDo:Add QRScanActivity

//ToDo:Add Hamburger Menu



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
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
        Timber.i("Placemark Activity started...")
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
                loadDummyData()
                Timber.i("DataLogViewer Scan QR selected")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // ---------------- Add dummy Data --------------------------------------------
private fun loadDummyData(){

        var mySiteData = ArrayList<SiteDataModel>()
        var mySite = SiteModel(data = mySiteData)
        var myKPI : SiteDataModel
        mySite.description = "My Test Site"
        mySite.id = app.sites.create(mySite)

        myKPI = SiteDataModel(1,"Temperature",R.drawable.temp,23.4,"Deg C")
        app.sites.addkpi(mySite.id,myKPI)
        myKPI = SiteDataModel(1,"Motor RPM",R.drawable.speedometer,800.0,"RPM")
        app.sites.addkpi(mySite.id,myKPI)
        myKPI = SiteDataModel(1,"Valve Status",R.drawable.valve,0.0,"OFF")
        app.sites.addkpi(mySite.id,myKPI)
        myKPI = SiteDataModel(1,"Flow Rate",R.drawable.flow,125.0,"L/m")
        app.sites.addkpi(mySite.id,myKPI)
}

}


// -----------------------------------------------------------------------------------------------------
