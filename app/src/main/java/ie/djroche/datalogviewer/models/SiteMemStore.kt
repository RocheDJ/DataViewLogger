package ie.djroche.datalogviewer.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class SiteMemStore : SiteStore {

    val  sites = ArrayList<SiteModel>()

    override fun findAll(): List<SiteModel> {
        return sites
    }

    override fun create(placemark: SiteModel) {
        placemark.id = getId()
        sites.add(placemark)
        logAll()
    }

    override fun update(placemark: SiteModel) {
        var foundSite: SiteModel? = sites.find { p -> p.id == placemark.id }
        if (foundSite != null) {
            foundSite.description = placemark.description
            //ToDo: add the updated data filelds
            logAll()
        }
    }

    fun logAll() {
        sites.forEach{ Timber.i("${it}") }
    }

}
