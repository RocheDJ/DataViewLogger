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
import ie.djroche.datalogviewer.helpers.MessageBox
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
    var xEdit : Boolean =false
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
        xEdit=false
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
       UpdateMenuIcons()
        return super.onCreateOptionsMenu(menu)
    }
    // -------------------   Process the Click ----------------------------
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_Back -> {
                finish()
            }
            R.id.item_Edit-> {
                xEdit =true

                binding.etDescription.setTextIsSelectable(xEdit);
                binding.etDescription.isEnabled = xEdit
                UpdateMenuIcons()
                Timber.i("Edit site Pressed")
            }
            R.id.item_Done->{
                xEdit =false
                binding.etDescription.setTextIsSelectable(xEdit);
                binding.etDescription.isEnabled = xEdit
                UpdateMenuIcons()
                UpdateSiteDescription()
            }
            R.id.item_AddKPI -> {
                showScanQR()
            }
            R.id.item_DeleteSite -> {
                DeleteSite()
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
    // -------------------------------------------------------------------------------------------
    private fun DeleteSite() {
        try {
            MessageBox(this).show(selectedSite.description,
                getString(R.string.confirm_delete_site)) {
                    if(it == MessageBox.ResponseType.YES){
                        app.sites.delete(selectedSite)
                        Timber.i("Delete Site ")
                        finish()
                    }else{
                        Timber.i("Delete Site Cancelled ")
                    }
            }
        } catch (e: Exception) {
            Timber.i("DeleteSite Error " + e.message)
        }
    }

    // -------------------------------------------------------------------------------------------
    // show different menu icons depending on user logedd in status
    private fun UpdateMenuIcons(){
        if (xEdit){
            binding.toolbar.menu.getItem(0).isVisible = false// F1 back
            binding.toolbar.menu.getItem(1).isVisible = false// F2 edit
            binding.toolbar.menu.getItem(2).isVisible = true// F3 done
            binding.toolbar.menu.getItem(3).isVisible = true// F4 Add KPI
            binding.toolbar.menu.getItem(4).isVisible = true// F5 delete
        } else {
            binding.toolbar.menu.getItem(0).isVisible = true// F1 back
            binding.toolbar.menu.getItem(1).isVisible = true// F2 edit
            binding.toolbar.menu.getItem(2).isVisible = false// F3 done
            binding.toolbar.menu.getItem(3).isVisible = false// F4 Add KPI
            binding.toolbar.menu.getItem(4).isVisible = false// F5 delete
        }
    }
}