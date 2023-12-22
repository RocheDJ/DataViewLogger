package ie.djroche.datalogviewer.ui.site

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.UserModel
import timber.log.Timber
import java.lang.Exception

class SiteViewModel (app: Application) : AndroidViewModel(app) {
    // declare the list of sites
    private var siteList =
        MutableLiveData<List<SiteModel>>()
    
    // list of sites as observable by the view model
    val observableSiteList: LiveData<List<SiteModel>>
        get() = siteList

    private var _site =
        MutableLiveData<SiteModel>()

    var liveSite = MutableLiveData<SiteModel?>()

    val observableSite: MutableLiveData<SiteModel>
        get() = _site

    var currentLiveUser = MutableLiveData<UserModel>()

    var mainApp: MainApp =  app as MainApp
    //-----------------------------------------------------------------------------
    init {
        load()
    }
    //-----------------------------------------------------------------------------
    fun load() {
        try {

           //SiteManager.findAll(siteList)
            mainApp.site_Manager.findAllForUser(currentLiveUser.value?.uid!!,siteList)
            //FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, donationsList)
            Timber.i("Report Load Success : ${siteList.value.toString()}")
        } catch (e: Exception) {
            Timber.e("Report Load Error : $e.message")
        }
    }
    //-----------------------------------------------------------------------------
    fun findByQR(userid: String, id: String){
        try {
            mainApp.site_Manager.findByQR(userid,id,_site)
            if (_site.value != null){
                liveSite.postValue(_site.value)
            }
            Timber.i("Site findByQR Called")
        } catch (e: Exception) {
            Timber.e("Site findByQR Error : $e.message")
        }
    }
    //-----------------------------------------------------------------------------
    fun delete(userid: String, id: String) {
        try {
            val site =  mainApp.site_Manager.find(id)
            mainApp.site_Manager.delete(site!!)
            // FirebaseDBManager.delete(userid,id)
            Timber.i("Site Delete Called")
        } catch (e: Exception) {
            Timber.i("Site Delete Error : $e.message")
        }
    }

    fun addSite( site: SiteModel) {
        try {
            mainApp.site_Manager.create(site)

            Timber.i("Site Add Called")
        } catch (e: Exception) {
            Timber.e("Site Add Error : $e.message")
        }
    }
    //-----------------------------------------------------------------------------
    fun clearSite()
    {
        _site.value!!.id= null!!
    }

}