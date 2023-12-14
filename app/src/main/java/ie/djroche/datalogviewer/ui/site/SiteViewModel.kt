package ie.djroche.datalogviewer.ui.site

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.djroche.datalogviewer.models.SiteManager
import ie.djroche.datalogviewer.models.SiteModel
import timber.log.Timber
import java.lang.Exception

class SiteViewModel : ViewModel() {
    // declare the list of sites
    private var siteList =
        MutableLiveData<List<SiteModel>>()

    // list of sites as observable by the view model
    val observableSiteList: LiveData<List<SiteModel>>
        get() = siteList

    //-----------------------------------------------------------------------------
    init {
        load()
    }
    //-----------------------------------------------------------------------------
    fun load() {
        try {
            SiteManager.findAll(siteList)
            //FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, donationsList)
            Timber.i("Report Load Success : ${siteList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    //-----------------------------------------------------------------------------
    fun delete(userid: String, id: String) {
        try {
            val site = SiteManager.find(id)
            SiteManager.delete(site!!)
            // FirebaseDBManager.delete(userid,id)
            Timber.i("Site Delete Called")
        } catch (e: Exception) {
            Timber.i("Site Delete Error : $e.message")
        }
    }


}