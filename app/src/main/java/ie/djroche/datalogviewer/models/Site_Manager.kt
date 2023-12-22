package ie.djroche.datalogviewer.models

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.File
import java.lang.reflect.Type


const val JSON_FILE = "sites.json"
val listKpiType: Type = object : TypeToken<ArrayList<SiteKPIModel>>() {}.type

class Site_Manager(application: Application) : SiteStore  {
    // JSON model decelerations
    val listType: Type = object : TypeToken<ArrayList<SiteModel>>() {}.type

    val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
        .create()
    private var sites = mutableListOf<SiteModel>()
    private var isFirebase: Boolean = false
    private var application: Application = Application()
    private var preferences: SharedPreferences

    init{
        this.application = application
        preferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
        val xFirebase = preferences.getBoolean("FireBase", false)
        isFirebase = xFirebase

        if (!xFirebase) {
            deserialize()
        }
    }

    override fun findAll(siteList: MutableLiveData<List<SiteModel>>) {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            siteList.value = sites
        }
    }

    override fun findByQR(userID: String, QRCode: String, site: MutableLiveData<SiteModel>) {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            val foundSite: SiteModel? = sites.find { p -> p.qrcode == QRCode && p.userid == userID }
            if (foundSite != null)
            {
                site.value = foundSite
            }
        }
    }

    override fun update(site: SiteModel) {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            val foundSite: SiteModel? = sites.find { p -> p.id == site.id }
            if (foundSite != null) {
                foundSite.description = site.description
                foundSite.data = site.data
                Timber.i("update  sites : $sites")
                serialize()
            }

        }
    }

    override fun findAllForUser(userID: String, siteList: MutableLiveData<List<SiteModel>>) {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            val foundSites = mutableListOf<SiteModel>()
            // manual search
            Timber.i("find all  sites : $sites")
            for (site in sites) {
                if (site.userid == userID) {
                    foundSites.add(site)
                }
            }
            siteList.value= foundSites
        }
    }

    override fun find(siteID: String): SiteModel? {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            val foundSite: SiteModel? = sites.find { p -> p.id == siteID }
            return foundSite
        }
    }

    override fun create(site: SiteModel): String {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            sites.add(site)
            serialize()
            return site.id
        }
    }

    override fun delete(site: SiteModel) {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            sites.remove(site)
            serialize()
        }
    }

    override fun addKPI(userID: String, siteID: String, aKPI: SiteKPIModel) {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            val foundSite: SiteModel? = sites.find { p -> p.id == siteID }
            if (foundSite != null) {
                foundSite.data.add(aKPI.copy())
                serialize()
            }else {
                Timber.i("AddKPI  Error : Not Found")
            }
        }
    }

    override fun getKPI(siteID: String, siteKPIList: MutableLiveData<List<SiteKPIModel>>) {
        if(isFirebase){
            TODO("Not yet implemented")
        } else {
            val foundSite: SiteModel? = sites.find { p -> p.id == siteID }
            if (foundSite != null) {
                siteKPIList.value= foundSite.data
            }else {
                siteKPIList.value= null
            }
        }
    }
    /*-------------                      JSON Private Functions         -------------------------------*/

    private fun serialize() {
        val  path = application?.applicationContext?.filesDir?.absolutePath
        val jsonString = gsonBuilder.toJson(sites, listType)
        File("$path/$JSON_FILE").writeText(jsonString)
    }

    private fun deserialize() {
        try {
            val  path = application?.applicationContext?.filesDir?.absolutePath
            val jsonString = File("$path/$JSON_FILE").readText()
            sites = gsonBuilder.fromJson(jsonString, listType)
        } catch (e: Exception) {
            Timber.i("User deserialize error : $e.message")
        }

    }


}