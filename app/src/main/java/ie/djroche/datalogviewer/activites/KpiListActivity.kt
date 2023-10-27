package ie.djroche.datalogviewer.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.GridView
import com.google.android.material.snackbar.Snackbar
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.adaptors.GridRVAdapter
import ie.djroche.datalogviewer.databinding.ActivityKpiListBinding
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteKPIModel
import timber.log.Timber

class KpiListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKpiListBinding

    // variables for grid view and kpi list
    lateinit var kpiGRV: GridView
    lateinit var kpiList: List<SiteKPIModel>
    lateinit var app: MainApp

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
        kpiList = ArrayList<SiteKPIModel>()
        // reference to main app
        app = application as MainApp


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
            //ToDo: add action for grid item click
            Snackbar.make( kpiGRV,kpiList[position].title + R.string._grid_tile_selected, Snackbar.LENGTH_LONG)
                .show()
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
}