package ie.djroche.datalogviewer.ui.site

import android.app.AlertDialog
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
import androidx.navigation.ui.NavigationUI

class SiteFragment : Fragment(), SiteClickListener {
    private var _fragBinding: FragmentSiteBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val siteViewModel : SiteViewModel by activityViewModels()
    lateinit var loader : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentSiteBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

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
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)


        fragBinding.fabScan.setOnClickListener {
            //ToDo: add QR Scan Action
            Timber.i("QR Scan Pressed from site")
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
        val action =  SiteFragmentDirections.actionSiteFragmentToKPIFragment(site.id)
        findNavController().navigate(action)

    }

}