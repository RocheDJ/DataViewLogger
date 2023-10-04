package ie.djroche.datalogviewer.models

interface SiteStore {
    fun update(site: SiteModel)
    fun findAll(): List<SiteModel>
    fun create(site: SiteModel):Long
    fun addkpi(siteID:Long,kpiData:SiteDataModel)
}