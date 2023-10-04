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

    override fun create(site: SiteModel): Long {
        site.id = getId()
        sites.add(site)
        return site.id
    }

    override fun addkpi(siteID: Long, kpiData: SiteDataModel) {
        var foundSite: SiteModel? = sites.find { p -> p.id == siteID }
        if (foundSite != null) {
            foundSite.data.add(kpiData.copy())
        }
    }

    override fun update(site: SiteModel) {
        var foundSite: SiteModel? = sites.find { p -> p.id == site.id }
        if (foundSite != null) {
            foundSite.description = site.description
            //ToDo: add the updated data filelds
            logAll()
        }
    }

    fun logAll() {
        sites.forEach{ Timber.i("${it}") }
    }

}
