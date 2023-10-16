package ie.djroche.datalogviewer.models

interface SiteStore {
    fun update(site: SiteModel)
    fun findAll(): List<SiteModel>
    fun find(siteID: String): SiteModel?
    fun findByQR(QRCode: String): SiteModel?
    fun create(site: SiteModel):String
    fun getkpi(siteID:String):MutableList<SiteKPIModel>?
}