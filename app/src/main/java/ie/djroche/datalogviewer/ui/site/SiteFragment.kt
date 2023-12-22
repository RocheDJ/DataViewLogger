package ie.djroche.datalogviewer.ui.site

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.activities.QRScanActivity
import ie.djroche.datalogviewer.adaptors.SiteAdaptor
import ie.djroche.datalogviewer.adaptors.SiteClickListener
import ie.djroche.datalogviewer.adaptors.SiteItemDecoration
import ie.djroche.datalogviewer.auth.LoggedInViewModel
import ie.djroche.datalogviewer.auth.LoginRegisterViewModel
import ie.djroche.datalogviewer.databinding.FragmentSiteBinding
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.utils.SwipeToDeleteCallback
import ie.djroche.datalogviewer.utils.createLoader
import ie.djroche.datalogviewer.utils.hideLoader
import ie.djroche.datalogviewer.utils.showLoader
import timber.log.Timber
import java.util.Locale

class SiteFragment : Fragment(), SiteClickListener {
    private var _fragBinding: FragmentSiteBinding? = null
    private val fragBinding get() = _fragBinding!!
    //private val siteViewModel: SiteViewModel by activityViewModels()
    private lateinit var siteViewModel: SiteViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    lateinit var loader: AlertDialog
    private var allowQrSelect: Boolean = false // toggle to stop navigation to kpi fragment twice
    // from live data


    // -------------------------------- Register Activity for QR Scan -----------------------------
    private val intentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val scannedQR = result.data?.getStringExtra("scannedQR")
                if (scannedQR != null) {
                    processQRScan(scannedQR)
                }
                Timber.i("QR Observer registerForActivityResult = $scannedQR")
            }
        }

    //-----------------------------------------------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentSiteBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        //Loading Dialogue
        loader = createLoader(requireActivity())
        // decoration for highlighting selected site
        fragBinding.recyclerView.addItemDecoration(SiteItemDecoration(fragBinding.root.context))
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        // load the view models
        siteViewModel = ViewModelProvider(this).get(SiteViewModel::class.java)

        //download list of sites on user change
        showLoader(loader, "Downloading Sites")
        loggedInViewModel.liveUser.observe(viewLifecycleOwner, Observer { myLiveUser ->
            if (myLiveUser != null) {
                siteViewModel.currentLiveUser.value = myLiveUser
                siteViewModel.load()
            }
        })

        loggedInViewModel.liveUser.value = loggedInViewModel.liveUser.value
        siteViewModel.observableSiteList.observe(viewLifecycleOwner, Observer { sites ->
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
                showLoader(loader, "Deleting Site")
                val adapter = fragBinding.recyclerView.adapter as SiteAdaptor
                adapter.removeAt(viewHolder.adapterPosition)
                siteViewModel.delete(
                    "123", (viewHolder.itemView.tag as SiteModel).id
                )
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
        // listen for the fragment button press
        fragBinding.fabAddSite.setOnClickListener {
            Timber.i("Add Site Clicked")
            addNewSite()
        }

        // listen for data change on site
        siteViewModel.observableSite.observe(viewLifecycleOwner,
            Observer<SiteModel?> { site ->
                if (site != null) {
                    // do once the easy way
                    if (allowQrSelect) {
                        allowQrSelect = false
                        selectSite(site)
                    }
                }
            })
        return root
    }
    /*-------------------------------------------------------------------------------------------*/

    private fun addNewSite() {
        val myNewSite: SiteModel = SiteModel(data = mutableListOf<SiteKPIModel>())
        myNewSite.description = "New Site"
        myNewSite.userid = loggedInViewModel.liveUser.value!!.uid.toString()
        myNewSite.data.add(SiteKPIModel())
        siteViewModel.addSite(loggedInViewModel.liveUser,myNewSite.copy())
        fragBinding.swiperefresh.isRefreshing = true
        showLoader(loader, "Downloading Site List")
        siteViewModel.load()
    }

    /*-------------------------------------------------------------------------------------------*/
    private fun setupMenu() {
        setHasOptionsMenu(true)
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)
                // below line is to get our menu item.
                val searchItem: MenuItem = menu.findItem(R.id.item_Search)

                val searchView: SearchView = searchItem.getActionView() as SearchView

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                    android.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(msg: String): Boolean {
                        // inside on query text change method we are
                        // calling a method to filter our recycler view.
                        filter(msg)
                        return false
                    }
                })

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.item_Search) {
                    Timber.i("Search clicked ")
                    return true
                }
                return NavigationUI.onNavDestinationSelected(
                    menuItem, requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /*-------------------------------------------------------------------------------------------*/
    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader, "Downloading Site List")
            siteViewModel.load()
        }
    }

    /*-------------------------------------------------------------------------------------------*/
    fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    /* ----------------------------------------------------------------------------------------- */
    private fun render(siteList: ArrayList<SiteModel>) {

        fragBinding.recyclerView.adapter = SiteAdaptor(siteList, this)

        if (siteList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.sitesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.sitesNotFound.visibility = View.GONE

            // if a site is selected highlight it in the list
            val adapter = fragBinding.recyclerView.adapter as SiteAdaptor
            if (siteViewModel.liveSite.value != null) {
                adapter.highlightSite(siteViewModel.liveSite.value!!, this.requireView())
            }

        }
    }

    /* ----------------------------------------------------------------------------------------- */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    /*-------------------------------------------------------------------------------------------*/
    override fun onSiteClick(site: SiteModel) {
        Timber.i("Site clicked " + site.description)
        allowQrSelect = true
        // note for this to work need androidx.navigation.safeargs in both gradle files
        siteViewModel.findByQR(loggedInViewModel.liveUser.value!!.uid.toString(),
            site.qrcode)

    }


    /*-------------------------------------------------------------------------------------------*/
    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading Sites")
        loggedInViewModel.liveUser.observe(viewLifecycleOwner, Observer { myLiveUser ->
            if (myLiveUser != null) {
                siteViewModel.currentLiveUser.value = myLiveUser
                siteViewModel.load()
            }
        })
        hideLoader(loader)
    }

    override fun onStart() {
        super.onStart()
    }

    /*-------------------------------------------------------------------------------------------*/
    private fun showScanQR() {
        Timber.i("DataLogViewer Scan QR selected")
        intentLauncher.launch(Intent(this.context, QRScanActivity::class.java))
    }

    /*-------------------------------------------------------------------------------------------*/
    private fun processQRScan(qrCode: String) {
        siteViewModel.findByQR(loggedInViewModel.liveUser.value!!.uid.toString(), qrCode)
        allowQrSelect = true
    }

    /*-------------------------------------------------------------------------------------------*/
    private fun selectSite(site: SiteModel) {
        val action =
            SiteFragmentDirections.actionSiteFragmentToKPIFragment(site.id,
                site.description,
                site)
        findNavController().navigate(action)
    }

    /*------------------------------------------------------------------------------------------*/
    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList: ArrayList<SiteModel> = ArrayList()
        val adapter = fragBinding.recyclerView.adapter as SiteAdaptor
        val siteList = siteViewModel.observableSiteList.value
        // running a for loop to compare elements.
        for (item in siteList!!) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.description.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this.context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredList)
        }
    }

}