package ie.djroche.datalogviewer.ui.site

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.djroche.datalogviewer.adaptors.SiteAdaptor
import ie.djroche.datalogviewer.adaptors.SiteClickListener
import ie.djroche.datalogviewer.databinding.FragmentSiteBinding
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.utils.SwipeToDeleteCallback
import ie.djroche.datalogviewer.utils.createLoader
import ie.djroche.datalogviewer.utils.hideLoader
import ie.djroche.datalogviewer.utils.showLoader
import timber.log.Timber
import androidx.navigation.fragment.findNavController

import ie.djroche.datalogviewer.activities.QRScanActivity

class SiteFragment : Fragment(), SiteClickListener {
    private var _fragBinding: FragmentSiteBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val siteViewModel : SiteViewModel by activityViewModels()
    lateinit var loader : AlertDialog

    // register for QR Scan result
    // https://stackoverflow.com/questions/14785806/android-how-to-make-an-activity-return-results-to-the-activity-which-calls-it
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentSiteBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        //  the site List info
        showLoader(loader,"Downloading Sites")
        siteViewModel.observableSiteList.observe(viewLifecycleOwner, Observer {
                sites ->
            sites?.let {
                render(sites as ArrayList<SiteModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()
        // swipe to delete
        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Site")
                val adapter = fragBinding.recyclerView.adapter as SiteAdaptor
                adapter.removeAt(viewHolder.adapterPosition)
                siteViewModel.delete(
                    "123",(viewHolder.itemView.tag as SiteModel).id)

                hideLoader(loader)
            }
        }
        // Delete
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)





        // listen for the fragment button press
        fragBinding.fabScan.setOnClickListener {
            Timber.i("QR Scan Pressed from site")
            showScanQR()
        }

        return root
    }
    /*------------------------------------------------------------------------------------------------*/
    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Site List")
            siteViewModel.load()
        }
    }
/*------------------------------------------------------------------------------------------------*/
    fun checkSwipeRefresh() {
    if (fragBinding.swiperefresh.isRefreshing)
        fragBinding.swiperefresh.isRefreshing = false
    }

    /* ------------------------------------------------------------------------------------------ */
    private fun render(siteList: ArrayList<SiteModel>) {

        fragBinding.recyclerView.adapter = SiteAdaptor(siteList,this)
        if (siteList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.sitesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.sitesNotFound.visibility = View.GONE
        }

    }
    /* -------------------------------------------------------------------------------------------- */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    /*---------------------------------------------------------------------------------------------*/
    override fun onSiteClick(site: SiteModel) {
        Timber.i("Site clicked " + site.description)
        // note for this to work need androidx.navigation.safeargs in both gradle files
        selectSite(site)
    }


    /*---------------------------------------------------------------------------------------------*/
    override fun  onResume(){
        super.onResume()

        // observer the selected site changed
        siteViewModel.observableSite.observe(viewLifecycleOwner, Observer {
                site ->
            site?.let {
                selectSite(it)
            }

        })
    }
    /*---------------------------------------------------------------------------------------------*/
    private fun showScanQR() {
        Timber.i("DataLogViewer Scan QR selected")
        intentLauncher.launch(Intent(this.context,  QRScanActivity::class.java))
    }
    /*---------------------------------------------------------------------------------------------*/
    private fun processQRScan(qrCode:String){
        siteViewModel.findByQR("1234",qrCode)
    }

    private fun selectSite(site: SiteModel){
        val action =  SiteFragmentDirections.actionSiteFragmentToKPIFragment(site.id,site.description)
        findNavController().navigate(action)
    }
}