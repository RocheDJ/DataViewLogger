package ie.djroche.datalogviewer.ui.kpi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ie.djroche.datalogviewer.main.MainApp

import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel

import timber.log.Timber
import java.lang.Exception


class KPIViewModel (app: Application):  AndroidViewModel(app) {
    // declare the list of sites
    private var kpiList =
        MutableLiveData<List<SiteKPIModel>>()

    // list of kpi as observable by the view model
    val observableKPIList: LiveData<List<SiteKPIModel>>
        get() = kpiList

    private val _SiteDescription =
        MutableLiveData<String>()

    val observableSiteDescription :LiveData<String>
        get() = _SiteDescription

    var mainApp: MainApp =  app as MainApp
    //-----------------------------------------------------------------------------
    fun getKPIs(userid:String, id: String) {
        try {
            mainApp.site_Manager.getKPI(userid,id,kpiList)// return the list of KPIS from
            Timber.i("Detail getKPIs() Success : ${kpiList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getKPIs() Error : $e.message")
        }
    }
    //-----------------------------------------------------------------------------
    fun delete(userid: String, id: String) {
        //ToDo: complete Delete
        try {
            val site = MutableLiveData<SiteModel>()
            mainApp.site_Manager.findById(userid,id,site)
            mainApp.site_Manager.delete(userid,site.value!!)
            Timber.i("Site Delete Called")
        } catch (e: Exception) {
            Timber.i("Site Delete Error : $e.message")
        }
    }
    //-----------------------------------------------------------------------------
    fun addKPI(userid: String, site: SiteModel,kpiData:SiteKPIModel) {
        mainApp.site_Manager.addKPI(userid,site,kpiData)
        Timber.i("KPI Add Called")
    }
    //----------------------------------------------------------------------------
    fun updateSiteDescription(userid: String, site: SiteModel,newDescription :String) {
        try {
            val MySite = site.copy()
            MySite.description = newDescription
            mainApp.site_Manager.update(userid,MySite)

            Timber.i("Site update called")
        } catch (e: Exception) {
            Timber.i("Site Update Error : $e.message")
        }
    }

}