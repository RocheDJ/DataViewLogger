package ie.djroche.datalogviewer.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.adaptors.GridRVAdapter
import ie.djroche.datalogviewer.databinding.ActivityKpiListBinding

import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteDataModel
import ie.djroche.datalogviewer.models.SiteModel
import timber.log.Timber

class KpiListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKpiListBinding

    // variables for grid view and kpi list
    lateinit var kpiGRV: GridView
    lateinit var kpiList: List<SiteDataModel>
    lateinit var app: MainApp
    var dummyQRId : Long =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKpiListBinding.inflate(layoutInflater)

        //set toolbar contents
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        // bind the activity View
        setContentView(binding.root)


        // initializing variables of grid view with their ids.
        kpiGRV = findViewById(R.id.idGRV)
        kpiList = ArrayList<SiteDataModel>()
        // reference to main app
        app = application as MainApp

        // populate the models with dummy data for now
        loadDummyData()
        // initialize our kpi adapter
        // and passing kpi list and context.

        // load with QR code Data
        //Todo: fix error here
       // val lQRcode :Long = app.qrCode.toLong() //dummyQRId
        val strQRCode : String = app.qrCode
        try {
            // some code
            kpiList = app.sites.findByQR(strQRCode)!!.data
            val kpiAdapter = GridRVAdapter(kpiList = kpiList , this@KpiListActivity)
            kpiGRV.adapter = kpiAdapter
        } catch (e: Exception) {
            Timber.i("KPI List Activity ERROR" + e.message)
        }

        // load the site Description
       // binding.tvDescription.text = app.sites.find(lQRcode)!!.description
        binding.tvDescription.text = app.sites.findByQR(strQRCode)!!.description
        //  adding on item
        // click listener for our grid view.
        kpiGRV.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // inside on click method we are simply displaying
            // a toast message with course name.
            Toast.makeText(
                applicationContext, kpiList[position].title + " selected",
                Toast.LENGTH_SHORT
            ).show()
        }


        Timber.i("KPI List Activity started...")

    }

    // ------------------   Load the Menu Items  --------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_kpilist, menu)
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
    // ---------------- Add dummy Data --------------------------------------------
    private fun loadDummyData(){

        var mySiteData = ArrayList<SiteDataModel>()
        var mySite = SiteModel(data = mySiteData)
        var myKPI : SiteDataModel
        mySite.description = "My Test Site 001"
        mySite.id = app.sites.create(mySite)
        dummyQRId =  mySite.id
        myKPI = SiteDataModel(1,"Temperature",R.drawable.temp,23.4,"Deg C")
        app.sites.addkpi(mySite.id,myKPI)
        myKPI = SiteDataModel(1,"Motor RPM",R.drawable.speedometer,800.0,"RPM")
        app.sites.addkpi(mySite.id,myKPI)
        myKPI = SiteDataModel(1,"Valve Status",R.drawable.valve,0.0,"OFF")
        app.sites.addkpi(mySite.id,myKPI)
        myKPI = SiteDataModel(1,"Flow Rate",R.drawable.flow,125.0,"L/m")
        app.sites.addkpi(mySite.id,myKPI)
        myKPI = SiteDataModel(1,"Tank Level",R.drawable.tank,65.0,"%")
        app.sites.addkpi(mySite.id,myKPI)

        var mySiteData_1 = ArrayList<SiteDataModel>()
        var mySite_1 = SiteModel(data = mySiteData_1)
        var myKPI_1 : SiteDataModel
        mySite_1.description = "My Test Site 002"
        mySite_1.id = app.sites.create(mySite_1)
        dummyQRId =  mySite_1.id
        myKPI_1 = SiteDataModel(1,"Valve Status",R.drawable.valve,0.0,"OFF")
        app.sites.addkpi(mySite_1.id,myKPI_1)
        myKPI_1 = SiteDataModel(1,"Flow Rate",R.drawable.flow,125.0,"L/m")
        app.sites.addkpi(mySite_1.id,myKPI_1)
        myKPI_1 = SiteDataModel(1,"Tank Level",R.drawable.tank,65.0,"%")
        app.sites.addkpi(mySite_1.id,myKPI_1)
    }
}