package ie.djroche.datalogviewer.models

interface SiteStore {
    fun update(placemark: SiteModel)
    fun findAll(): List<SiteModel>
    fun create(placemark: SiteModel)
}