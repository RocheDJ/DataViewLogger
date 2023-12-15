package ie.djroche.datalogviewer.models

import androidx.lifecycle.MutableLiveData

interface SiteStore {
    fun findAll(siteList:
                MutableLiveData<List<SiteModel>>)
    fun findByQR(QRCode: String): SiteModel?

    fun update(site: SiteModel)
    fun findAllForUser(userID:String): List<SiteModel>
    fun find(siteID: String): SiteModel?

    fun create(site: SiteModel):String
    fun delete(site: SiteModel)
    fun getKPI(siteID:String, siteKPIList:MutableLiveData<List<SiteKPIModel>>)
}