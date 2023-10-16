package ie.djroche.datalogviewer.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.Type
import java.util.Random
import ie.djroche.datalogviewer.helpers.*
import android.content.Context

const val JSON_FILE = "sites.json"

val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .create()

val listType: Type = object : TypeToken<ArrayList<SiteModel>>() {}.type

class SiteJSONStore(private val context: Context) : SiteStore {

    var sites = mutableListOf<SiteModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): List<SiteModel> {
        return sites
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
        sites.add(site.copy())
        serialize()
        return site.id
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
            //ToDo: add the updated data filelds
            logAll()
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

