package ie.djroche.datalogviewer.ui.kpi

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.activities.QRScanActivity
import ie.djroche.datalogviewer.adaptors.KPIGridClickListener
import ie.djroche.datalogviewer.adaptors.KPIGridViewAdaptor
import ie.djroche.datalogviewer.databinding.FragmentKPIBinding
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.gsonBuilder
import ie.djroche.datalogviewer.models.listKpiType
import ie.djroche.datalogviewer.utils.createLoader
import ie.djroche.datalogviewer.utils.hideLoader
import ie.djroche.datalogviewer.utils.showLoader
import timber.log.Timber

class KPIFragment : Fragment(), KPIGridClickListener {
    private var _fragBinding: FragmentKPIBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val kpiViewModel: KPIViewModel by activityViewModels()
    lateinit var loader: AlertDialog
    private val args by navArgs<KPIFragmentArgs>()

    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val scannedQR =  result.data?.getStringExtra("scannedQR")
                if (scannedQR != null) {
                    processQRScan(scannedQR)
                }
                Timber.i("QR Observer registerForActivityResult = $scannedQR")
            }
        }
    //----------------------------- Fragment View Creation -----------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentKPIBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader = createLoader(requireActivity())
        showLoader(loader, "Downloading KPI List")

        kpiViewModel.observableKPIList.observe(viewLifecycleOwner, Observer { kpis ->
            kpis?.let {
                hideLoader(loader)
                render(kpis as ArrayList<SiteKPIModel>)
            }
        })


        // Update Button to update site description
        fragBinding.updateSiteButton.setOnClickListener {
            kpiViewModel.updateSiteDescription("1234",args.siteID,fragBinding.siteDescriptionVM.toString())
            findNavController().navigateUp() // go back to site list
        }

        // Button to add kpi
        fragBinding.addKPIButton.setOnClickListener {
            showScanQR()
        }
        return root
    }

    // ---------------------------- Click on a grid element --------------------------------------------------------
    override fun onKPIGridClick(kpi: SiteKPIModel) {
        Timber.i("KPI Grid click " + kpi.title)
    }

    // ------------------------- RENDER the Fragment ---------------------------------------------------------
    private fun render(kpiList: ArrayList<SiteKPIModel>) {
        val kpiAdapter = KPIGridViewAdaptor(kpiList, this.requireContext().applicationContext, this)
        fragBinding.gridView.adapter =
            kpiAdapter //KPIGridViewAdaptor(kpiList,fragBinding.root.context,this)
        fragBinding.editSiteName.setText("---") // give the edit text an initial value

        if (kpiList.isEmpty()) {
            fragBinding.gridView.visibility = View.GONE
            fragBinding.kpisNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.gridView.visibility = View.VISIBLE
            fragBinding.kpisNotFound.visibility = View.GONE
        }
        fragBinding.siteDescriptionVM = kpiViewModel.observableSiteDescription.value
    }

    // ------------------------- RENDER the Menu for fragment--------------------------------------
    private fun setupMenu() {
        setHasOptionsMenu(true)
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_back, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if(menuItem.itemId == R.id.item_Back){
                    findNavController().popBackStack()
                    return true
                }
                return NavigationUI.onNavDestinationSelected(
                    menuItem, requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    //---------------------------- when Add KPI button pressed-------------------------------------
    private fun showScanQR() {
        Timber.i("DataLogViewer Scan QR selected")
        intentLauncher.launch(Intent(this.context,  QRScanActivity::class.java))
    }
    //---------------------------- when Add KPI button pressed-------------------------------------
    private fun processQRScan(qrCode:String){
        addKPI(qrCode)
    }

    // -------------------------------------------------------------------------------------------
    private fun addKPI(kpiJSONString: String) {
        try {
            var addKPIList: MutableList<SiteKPIModel> = mutableListOf<SiteKPIModel>()
            addKPIList = gsonBuilder.fromJson(kpiJSONString, listKpiType)
            for (kpi in addKPIList) {
                kpiViewModel.addKPI("1234",  args.siteID,kpi)
            }
        } catch (e: Exception) {
            Timber.i("addKPI Error " + e.message)
        }
    }
    // -------------------------- When view is resumed from idle ----------------------------------
    override fun onResume() {
        super.onResume()
        try {
            if (!args.siteID.isEmpty()) {
                kpiViewModel.getKPIs(
                    "12345",
                    args.siteID
                )
                fragBinding.siteDescriptionVM = args.siteDescription
            }
        } catch (e: Exception) {
            Timber.e("KPI Grid onResume error  " + e.message)
            hideLoader(loader)
        }
    }

    // ----------------------- when we go by by ----------------------------------------------
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}