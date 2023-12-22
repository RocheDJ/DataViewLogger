package ie.djroche.datalogviewer.models

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.io.File
import java.lang.reflect.Type


const val JSON_FILE = "sites.json"
val listKpiType: Type = object : TypeToken<ArrayList<SiteKPIModel>>() {}.type
var database: DatabaseReference = FirebaseDatabase.getInstance().reference

class Site_Manager(application: Application) : SiteStore  {
    // JSON model decelerations
    val listType: Type = object : TypeToken<ArrayList<SiteModel>>() {}.type
    val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
        .create()
    private var sites = mutableListOf<SiteModel>()  // for JSON
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

    override fun findAll(userID: String, siteList: MutableLiveData<List<SiteModel>>) {
        if(isFirebase){
            database.child("site-data").child(userID)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Timber.i("Firebase Site Data error : ${error.message}")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val localList = ArrayList<SiteModel>()
                        val children = snapshot.children
                        children.forEach {
                            val site = it.getValue(SiteModel::class.java)
                            localList.add(site!!.copy())
                        }

                        // need to do this detaching to avoid IllegalStateExceptions
                        database.child("site-data").child(userID)
                            .removeEventListener(this)
                        siteList.value = localList
                    }
                })
        } else {
            siteList.value = sites
        }
    }
//--------------------------------------------------------------------------------------------------
    override fun findByQR(userID: String, QRCode: String, site: MutableLiveData<SiteModel>) {
        if(isFirebase){
            try{
                // basic implementation to read all for given user and then search for QR
                database.child("user-site-data").child(userID)
                    .child(QRCode).get().addOnSuccessListener {
                        val MySite = it.getValue(SiteModel::class.java)
                        if (MySite!=null){
                            MySite.id = MySite.qrcode
                            site.value = MySite
                            Timber.i("FindByQR firebase Got value ${it.value}")
                        }
                    }.addOnFailureListener{
                        Timber.e("FindByQR Error getting data $it")
                    }
            }catch (e: Exception) {
                Timber.i("findByQR Error " + e.message)
            }
        } else {
            val foundSite: SiteModel? = sites.find { p -> p.qrcode == QRCode && p.userid == userID }
            if (foundSite != null)
            {
                site.value = foundSite
            }
        }
    }

    override fun update(userID: String,site: SiteModel) {
        if(isFirebase){
            val siteValues = site.toMap()
            database.child("user-site-data").child("$userID/${site.id}").setValue(siteValues)
            database.child("site-data").child(site.id).setValue(siteValues)
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
            database.child("user-site-data").child(userID)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Timber.i("Firebase findAllForUser Data error : ${error.message}")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val localList = ArrayList<SiteModel>()
                        val children = snapshot.children
                        children.forEach {
                            val site = it.getValue(SiteModel::class.java)
                            site!!.id = site.qrcode
                            localList.add(site.copy())
                        }

                        // need to do this detaching to avoid IllegalStateExceptions
                        database.child("site-data").child(userID)
                            .removeEventListener(this)
                        siteList.value = localList
                    }
                })
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

    override fun findById(userID: String,
                          siteID: String, outSite: MutableLiveData<SiteModel>) {
        if(isFirebase){
            database.child("user-site-data").child(userID)
                .child(siteID).get().addOnSuccessListener {
                    val foundSite: SiteModel? = it.getValue(SiteModel::class.java)
                    if (foundSite != null) {
                        foundSite.id = foundSite.qrcode
                        outSite.value =foundSite
                    }

                    Timber.i("firebase findById value ${outSite.value}")
                }.addOnFailureListener{
                    Timber.e("firebase Error getting data $it")
                }
        } else {
            val foundSite: SiteModel? = sites.find { p -> p.id == siteID }
            outSite.value = foundSite

        }
    }

    override fun create(user :MutableLiveData<UserModel>,
                        site: SiteModel): String
    {
        if(isFirebase){
            Timber.i("Firebase DB Reference : $database")
            val uid = user.value!!.uid
            val key = database.child("site-data").push().key
            if (key == null) {
                Timber.i("Firebase Error : Key Empty")
                return "-"
            }
            site.id = key
            site.qrcode =key // QR code and Id match for simplicity
            val siteValues = site.toMap()

            val childAdd = HashMap<String, Any>()
            childAdd["/site-data/$key"] = siteValues
            childAdd["/user-site-data/$uid/$key"] = siteValues

            database.updateChildren(childAdd)
            return site.id
        } else {
            site.qrcode = site.id // QR code and Id match for simplicity
            sites.add(site)
            serialize()
            return site.id
        }
    }

    override fun delete(userID: String, site: SiteModel) {
        if(isFirebase){
            val childDelete : MutableMap<String, Any?> = HashMap()
            childDelete["/site-data/${site.id}"] = null
            childDelete["/user-site-data/$userID/$site.id"] = null
        } else {
            sites.remove(site)
            serialize()
        }
    }

    override fun addKPI(userID: String, site: SiteModel, aKPI: SiteKPIModel) {
        if(isFirebase){
            //find the record , amend the kpi data save the record
            val  data= MutableLiveData<SiteKPIModel>()
            data.value = aKPI
            // find the site
            site.data.add(aKPI)

            val siteValues = site.toMap()
            database.child("user-site-data").child("$userID/${site.id}").setValue(siteValues)
            database.child("site-data").child(site.id).setValue(siteValues)

        } else {
            val foundSite: SiteModel? = sites.find { p -> p.id == site.id }
            if (foundSite != null) {
                foundSite.data.add(aKPI.copy())
                serialize()
            }else {
                Timber.i("AddKPI  Error : Not Found")
            }
        }
    }

    override fun getKPI(userID:String, siteID: String, siteKPIList: MutableLiveData<List<SiteKPIModel>>) {
        if(isFirebase){
            database.child("user-site-data").child(userID)
                .child(siteID).get().addOnSuccessListener {
                    val foundSite: SiteModel? = it.getValue(SiteModel::class.java)
                    if (foundSite != null) {
                        foundSite.id = foundSite.qrcode
                        siteKPIList.postValue(foundSite.data)
                    }
                }.addOnFailureListener{
                    Timber.e("getKPI Error getting data $it")
                }
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