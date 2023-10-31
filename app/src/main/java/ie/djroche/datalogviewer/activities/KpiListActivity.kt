package ie.djroche.datalogviewer.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.GridView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.adaptors.GridRVAdapter
import ie.djroche.datalogviewer.databinding.ActivityKpiListBinding
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.models.gsonBuilder
import ie.djroche.datalogviewer.models.listKpiType
import timber.log.Timber

class KpiListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKpiListBinding

    // variables for grid view and kpi list
    lateinit var kpiGRV: GridView
    lateinit var kpiList: List<SiteKPIModel>
    lateinit var app: MainApp
    var selectedSite = SiteModel()

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

        //retrieve the information for the site
        try {
            selectedSite = app.sites.findByQR(strQRCode)!!
            // some code
            kpiList = selectedSite.data
            val kpiAdapter = GridRVAdapter(kpiList = kpiList , this@KpiListActivity)
            kpiGRV.adapter = kpiAdapter

            // load the site Description dont allow editing until enabled
            binding.etDescription.setText(selectedSite.description)
            binding.etDescription.isEnabled = false

        } catch (e: Exception) {
            Timber.i("KPI List Activity ERROR" + e.message)
        }

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
            R.id.item_Edit-> {
                binding.etDescription.setTextIsSelectable(true);
                binding.etDescription.isEnabled = true
                binding.toolbar.menu.getItem(1).isVisible =false
                binding.toolbar.menu.getItem(2).isVisible =true
                Timber.i("Edit site Pressed")
            }
            R.id.item_Done->{
                binding.etDescription.setTextIsSelectable(false);
                binding.etDescription.isEnabled = false
                binding.toolbar.menu.getItem(1).isVisible =true
                binding.toolbar.menu.getItem(2).isVisible =false
                UpdateSiteDescription()
            }
            R.id.item_AddKPI -> {
                showScanQR()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun UpdateSiteDescription(){
        selectedSite.description = binding.etDescription.text.toString()
        app.sites.update(selectedSite.copy())
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
    private val getResultQRScan =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                Timber.i("QR Scan Returned OK")
                //ToDo: Validate the QR Code
                try {
                    if (app.qrCode != "-") {
                        addKPI(app.qrCode)
                    }
                } catch (e: Exception) {
                    Timber.i("getResultQRScan Error " + e.message)
                }
            }
        }

    // -------------------------------------------------------------------------------------------
    private fun addKPI(kpiJSONString: String) {
        try {
            var addKPIList: MutableList<SiteKPIModel> = mutableListOf<SiteKPIModel>()
            addKPIList = gsonBuilder.fromJson(kpiJSONString, listKpiType)
            for (kpi in addKPIList) {
                selectedSite.data.add(kpi)
                app.sites.update(selectedSite.copy())
                UpdateKPIList()
            }
        } catch (e: Exception) {
            Timber.i("addKPI Error " + e.message)
        }
    }

    // -------------------------------------------------------------------------------------------
    private fun UpdateKPIList() {
        kpiList = selectedSite.data
        val kpiAdapter = GridRVAdapter(kpiList = kpiList, this@KpiListActivity)
        kpiGRV.adapter = kpiAdapter
    }

}