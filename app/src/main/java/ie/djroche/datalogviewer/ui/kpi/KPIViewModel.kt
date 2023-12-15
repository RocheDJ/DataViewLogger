package ie.djroche.datalogviewer.ui.kpi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteManager
import timber.log.Timber
import java.lang.Exception


class KPIViewModel : ViewModel() {
    // declare the list of sites
    private var kpiList =
        MutableLiveData<List<SiteKPIModel>>()

    // list of kpi as observable by the view model
    val observableKPIList: LiveData<List<SiteKPIModel>>
        get() = kpiList
    
    //-----------------------------------------------------------------------------
    fun getKPIs(userid:String, id: String) {
        try {
            SiteManager.getKPI(id,kpiList)// return the list of KPIS from
            Timber.i("Detail getKPIs() Success : ${
                kpiList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getDonation() Error : $e.message")
        }
    }
    //-----------------------------------------------------------------------------
    fun delete(userid: String, id: String) {
        //ToDo: complete Delete
        try {
            val site = SiteManager.find(id)
            SiteManager.delete(site!!)
            // FirebaseDBManager.delete(userid,id)
            Timber.i("Site Delete Called")
        } catch (e: Exception) {
            Timber.i("Site Delete Error : $e.message")
        }
    }
    //-----------------------------------------------------------------------------
    fun addKPI(userid: String, id: String) {
        //ToDo: complete addKPI
        Timber.i("KPI Add Called")
    }
}