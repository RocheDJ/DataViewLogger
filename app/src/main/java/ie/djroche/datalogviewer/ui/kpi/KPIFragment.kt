package ie.djroche.datalogviewer.ui.kpi

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.adaptors.KPIGridClickListener
import ie.djroche.datalogviewer.adaptors.KPIGridViewAdaptor
import ie.djroche.datalogviewer.databinding.FragmentKPIBinding
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.ui.site.SiteViewModel
import ie.djroche.datalogviewer.utils.createLoader
import ie.djroche.datalogviewer.utils.hideLoader
import ie.djroche.datalogviewer.utils.showLoader
import timber.log.Timber

class KPIFragment : Fragment() ,KPIGridClickListener{
    private var _fragBinding: FragmentKPIBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val kpiViewModel : KPIViewModel by activityViewModels()
    lateinit var loader : AlertDialog
    private lateinit var viewModel: KPIViewModel
    private val args by navArgs<KPIFragmentArgs>()
    private val siteViewModel: SiteViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentKPIBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader = createLoader(requireActivity())

        showLoader(loader,"Downloading KPI List")

        kpiViewModel.observableKPIList.observe(viewLifecycleOwner, Observer {
                kpis ->
            kpis?.let {
                hideLoader(loader)
                render(kpis as ArrayList<SiteKPIModel>)
            }
        })

        fragBinding.fabScan.setOnClickListener {
            //ToDo: add QR Scan Action
            Timber.i("QR Scan Pressed from KPI")
        }

        return root
    }
    private fun render(kpiList: ArrayList<SiteKPIModel>) {
        val kpiAdapter = KPIGridViewAdaptor(kpiList, this.requireContext().applicationContext,this)
            fragBinding.gridView.adapter = kpiAdapter //KPIGridViewAdaptor(kpiList,fragBinding.root.context,this)

        if (kpiList.isEmpty()) {
            fragBinding.gridView.visibility = View.GONE
            fragBinding.kpisNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.gridView.visibility = View.VISIBLE
            fragBinding.kpisNotFound.visibility = View.GONE
           // fragBinding.site =
        }

    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_kpi, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onResume() {
        super.onResume()
        try {
            if ( !args.siteID.isEmpty()){
                kpiViewModel.getKPIs(
                    "12345",
                    args.siteID)
            }
        } catch (e : Exception) {
            Timber.e("KPI Grid onResume error  " + e.message )
            hideLoader(loader)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onKPIGridClick(kpi: SiteKPIModel) {
        Timber.i("KPI Grid click " + kpi.title )
    }
}