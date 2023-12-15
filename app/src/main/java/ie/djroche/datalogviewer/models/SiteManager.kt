package ie.djroche.datalogviewer.models

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import timber.log.Timber
import java.io.File


object SiteManager : SiteStore {
    // temp site manager for jason local file
    // reads file on each call ..
    //the path is data/user/0/ie.djroche.datalogviewer/files from applicationContext.filesDir.absolutePath
    private const val JSON_FILE = "data/user/0/ie.djroche.datalogviewer/files/sites.json"
    private var sites = mutableListOf<SiteModel>()
    private val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
        .create()
    override fun findAll(siteList: MutableLiveData<List<SiteModel>>){
        deserialize()
        siteList.value = sites
    }

    override fun findAllForUser(userID: String): List<SiteModel> {
        deserialize()
        var foundSites = mutableListOf<SiteModel>()
        // manual search
        for (site in sites) {
            if (site.userid == userID) {
                foundSites.add(site)
            }
        }
        return foundSites
    }

    override fun find(siteID: String): SiteModel? {
        deserialize()
        val foundSite: SiteModel? = sites.find { p -> p.id == siteID }
        return foundSite
    }

    override fun findByQR(QRcode: String): SiteModel? {
        deserialize()
        val foundSite: SiteModel? = sites.find { p -> p.qrcode == QRcode }
        return foundSite
    }

    override fun create(site: SiteModel): String {
        deserialize()
        sites.add(site)
        serialize()
        return site.id
    }

    override fun delete(site: SiteModel) {
        deserialize()
        sites.remove(site)
        serialize()
    }

    override fun getKPI(siteID: String, siteKPIList : MutableLiveData<List<SiteKPIModel>>){
        deserialize()
        val foundSite: SiteModel? = sites.find { p -> p.id == siteID }
        if (foundSite != null) {
            siteKPIList.value= foundSite.data
        }else {
            siteKPIList.value= null
        }

    }

    override fun update(site: SiteModel) {
        deserialize()
        var foundSite: SiteModel? = sites.find { p -> p.id == site.id }
        if (foundSite != null) {
            foundSite.description = site.description
            foundSite.data = site.data

            serialize()
        }
    }
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(sites, listType)
        File( JSON_FILE).writeText(jsonString)
    }

    private fun deserialize() {
        try{
            val jsonString =File(JSON_FILE).readText()
            sites = gsonBuilder.fromJson(jsonString, listType)
        }catch(e:Exception){

            Timber.i("User deserialize error : $e.message")
        }
    }
}