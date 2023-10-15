package ie.djroche.datalogviewer.models

import timber.log.Timber



class SiteMemStore : SiteStore {
    var lastId = 1L

    internal fun getId(): Long {
        return lastId++
    }

    val  sites = ArrayList<SiteModel>()

    override fun findAll(): List<SiteModel> {
        return sites
    }

    override fun find(siteID: Long): SiteModel? {
        var foundSite: SiteModel? = sites.find { p -> p.id == siteID }
        return foundSite
    }

    override fun findByQR(QRcode: String): SiteModel? {
        var foundSite: SiteModel? = sites.find { p -> p.qrcode == QRcode }
        return foundSite
    }


    override fun create(site: SiteModel): Long {
        site.id = getId()
        sites.add(site)
        return site.id
    }


    override fun getkpi(siteID: Long): MutableList<SiteDataModel>? {
        var foundSite: SiteModel? = sites.find { p -> p.id == siteID }
        if (foundSite != null) {
           return foundSite.data
        }else {
            return null
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
