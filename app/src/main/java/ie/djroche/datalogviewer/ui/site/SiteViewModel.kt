package ie.djroche.datalogviewer.ui.site

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.djroche.datalogviewer.models.SiteManager
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.models.UserModel
import timber.log.Timber
import java.lang.Exception

class SiteViewModel : ViewModel() {
    // declare the list of sites
    private var siteList =
        MutableLiveData<List<SiteModel>>()
    
    // list of sites as observable by the view model
    val observableSiteList: LiveData<List<SiteModel>>
        get() = siteList

    private var _site =
        MutableLiveData<SiteModel?>()

    var liveSite = MutableLiveData<SiteModel?>()

    val observableSite: MutableLiveData<SiteModel?>
        get() = _site

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
    fun findByQR(userid: String, id: String){
        try {

            val mySite = SiteManager.findByQR(id)
            if (mySite!=null){
                liveSite.postValue(mySite)
                _site.value=mySite
            }
            Timber.i("Site findByQR Called")
        } catch (e: Exception) {
            Timber.i("Site findByQR Error : $e.message")
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

    fun addSite( site: SiteModel) {
        try {
            SiteManager.create(site)

            Timber.i("Site Add Called")
        } catch (e: Exception) {
            Timber.i("Site Add Error : $e.message")
        }
    }
    //-----------------------------------------------------------------------------
    fun clearSite()
    {
        _site.value!!.id= null!!
    }

}