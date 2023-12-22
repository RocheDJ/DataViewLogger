package ie.djroche.datalogviewer.models

import androidx.lifecycle.MutableLiveData

interface SiteStore_old {
    fun findAll(siteList:
                MutableLiveData<List<SiteModel>>)

    fun findByQR(userID: String,QRCode: String): SiteModel?

    fun update(site: SiteModel)
    fun findAllForUser(userID:String,
                       siteList:
                       MutableLiveData<List<SiteModel>>)
    fun find(siteID: String): SiteModel?

    fun create(site: SiteModel):String
    fun delete(site: SiteModel)
    fun addKPI(userID:String,siteID:String, aKPI:SiteKPIModel)

    fun getKPI(siteID:String, siteKPIList:MutableLiveData<List<SiteKPIModel>>)
}