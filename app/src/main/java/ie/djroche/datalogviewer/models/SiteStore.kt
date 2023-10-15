package ie.djroche.datalogviewer.models

interface SiteStore {
    fun update(site: SiteModel)
    fun findAll(): List<SiteModel>
    fun find(siteID: Long): SiteModel?
    fun findByQR(QRCode: String): SiteModel?
    fun create(site: SiteModel):Long
    fun getkpi(siteID:Long):MutableList<SiteDataModel>?

}