package ie.djroche.datalogviewer.models

import androidx.lifecycle.MutableLiveData

interface SiteStore {
    fun findAll(userID: String,
                siteList: MutableLiveData<List<SiteModel>>)
    fun findByQR(userID: String,
                 QRCode: String,
                 site  : MutableLiveData<SiteModel>)

    fun update(userID: String,
               site: SiteModel)

    fun findAllForUser(userID:String,
                       siteList:
                       MutableLiveData<List<SiteModel>>)
    fun findById(userID:String,
                 siteID: String,
                 site  : MutableLiveData<SiteModel>)

    fun create(user :MutableLiveData<UserModel>,
               site: SiteModel):String

    fun delete(userID:String,
               site: SiteModel)

    fun addKPI(userID:String,site:SiteModel, aKPI:SiteKPIModel)

    fun getKPI(userID:String,siteID:String, siteKPIList:MutableLiveData<List<SiteKPIModel>>)

}