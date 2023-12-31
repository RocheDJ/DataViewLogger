package ie.djroche.datalogviewer.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.Type
import ie.djroche.datalogviewer.helpers.*
import android.content.Context
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.main.MainApp

const val JSON_FILE = "sites.json"

val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .create()

val listType: Type = object : TypeToken<ArrayList<SiteModel>>() {}.type
val listKpiType: Type = object : TypeToken<ArrayList<SiteKPIModel>>() {}.type
class SiteJSONStore(private val context: Context,private val userID : String) : SiteStore {

    var sites = mutableListOf<SiteModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }else{
            // create the file and read it
            loadDummyJSONSiteData(context,userID)
            deserialize()
        }
    }

    override fun findAll(): List<SiteModel> {
        return sites
    }

    override fun findAllForUser(userID: String): List<SiteModel> {
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
        var foundSite: SiteModel? = sites.find { p -> p.id == siteID }
        return foundSite
    }

    override fun findByQR(QRcode: String): SiteModel? {
        var foundSite: SiteModel? = sites.find { p -> p.qrcode == QRcode }
        return foundSite
    }


    override fun create(site: SiteModel): String {
        sites.add(site)
        serialize()
        return site.id
    }

    override fun delete(site: SiteModel) {
        sites.remove(site)
        serialize()
    }

    override fun getkpi(siteID: String): MutableList<SiteKPIModel>? {
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
            foundSite.data = site.data
            //ToDo: add the updated data filelds
            logAll()
            serialize()
        }
    }

    fun logAll() {
        sites.forEach{ Timber.i("${it}") }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(sites, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        sites = gsonBuilder.fromJson(jsonString, listType)
    }

}

