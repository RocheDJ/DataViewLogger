package ie.djroche.datalogviewer.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.djroche.datalogviewer.models.JSON_FILE
import ie.djroche.datalogviewer.models.JSON_USERFILE
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.models.UserModel
import timber.log.Timber
import java.lang.reflect.Type
import java.math.BigInteger
import java.security.MessageDigest


fun loadDummyJSONData(context: Context) {
    var dummyUsers = mutableListOf<UserModel>()
    var myNewUser_1: UserModel = UserModel()
    var myNewUser_2: UserModel = UserModel()
    val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
        .create()
    val listType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type
    try {
        myNewUser_1.email = "homer@simpson.com"
        myNewUser_1.firstName = "homer"
        myNewUser_1.lastName = "simpson"
        myNewUser_1.password = encryptString("secret")

        dummyUsers.add(myNewUser_1.copy())

        loadDummyJSONSiteData(context,myNewUser_1.uid!!)

        myNewUser_2.email = "marge@simpson.com"
        myNewUser_2.firstName = "marge"
        myNewUser_2.lastName = "simpson"
        myNewUser_2.password = encryptString("secrettoo")

        dummyUsers.add(myNewUser_2.copy())

        val jsonString = gsonBuilder.toJson(dummyUsers, listType)
        write(context, JSON_USERFILE, jsonString)

    } catch (e: Exception) {
        Timber.e("Cannot loadDummy Site Data: %s", e.toString());
    }
}
//-----------------------------------------------------------------------------------------------------
fun loadDummyJSONSiteData(context: Context,userID :String) {
    var myNewSite: SiteModel = SiteModel(data = mutableListOf<SiteKPIModel>())
    var myNewSite1: SiteModel = SiteModel(data = mutableListOf<SiteKPIModel>())
    var dummySites = mutableListOf<SiteModel>()
    val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
        .create()
    val listType: Type = object : TypeToken<ArrayList<SiteModel>>() {}.type
    var id: String
    try {
        // create the site data for 1st site
        myNewSite.description = "Homer Site 001"
        myNewSite.data.add(SiteKPIModel(1, "Temperature", "temp.png", 23.4, "Deg C").copy())
        myNewSite.data.add(SiteKPIModel(1, "Conductivity", "conductivity.png", 50.5, "%").copy())
        myNewSite.data.add(
            SiteKPIModel(
                2,
                "Motor RPM",
               "speedometer.png",
                800.0,
                "RPM"
            ).copy()
        )
        myNewSite.data.add(SiteKPIModel(3, "Valve Status", "valve.png", 0.0, "OFF").copy())
        myNewSite.data.add(SiteKPIModel(4, "Flow Rate", "flow.png", 125.0, "L/m").copy())
        myNewSite.data.add(SiteKPIModel(5, "Tank Level", "tank.png", 65.0, "%").copy())
        myNewSite.qrcode = "QR-000006-000001"
        myNewSite.userid = userID
        dummySites.add(myNewSite.copy())

        // create the site data for 2nd site
        myNewSite1.description = "Homer Site 002"
        myNewSite1.data.add(SiteKPIModel(3, "Temperature", "temp.png", 13.4, "Deg C").copy())
        myNewSite1.data.add(
            SiteKPIModel(
                4,
                "Motor RPM",
                "speedometer.png",
                100.0,
                "RPM"
            ).copy()
        )
        myNewSite1.qrcode = "QR-000006-000002"
        myNewSite1.userid = userID
        dummySites.add(myNewSite1.copy())
        val jsonString = gsonBuilder.toJson(dummySites, listType)
        write(context, JSON_FILE, jsonString)

    } catch (e: Exception) {
        Timber.e("Cannot loadDummy Site Data: %s", e.toString());
    }
}
//--------------------------------------------------------------------------------------------------------------
// reference URL https://www.knowledgefactory.net/2021/01/kotlin-hashing.html
//--------------------------------------------------------------------------------------------------------------

fun encryptString(input : String ):String {
    var retValue: String =""
    try {
            val md = MessageDigest.getInstance("SHA-1") // Encryption type.
            val messageDigest = md.digest(input.toByteArray())
            val no = BigInteger(1, messageDigest)
            var hashtext = no.toString(16)
            while (hashtext.length < 32) {
                hashtext = "0$hashtext"
            }
        retValue = hashtext.toString()
        } catch (e: Exception) {
            timber.log.Timber.e("EncryptString Error %s", e.toString())
        }
    return retValue
}
//--------------------------------------------------------------------------------------------------------------
fun ValidateUser(user :UserModel, enteredPassword :String) : Int{
    var iReturn =-1
    try {
        val encryptedPassword :String = encryptString(input=enteredPassword)
        iReturn = if (encryptedPassword == user.password){
            0
        }else{
            1
        }
    }catch (e: Exception){
        timber.log.Timber.e("ValidateUser Error %s", e.toString())
    }
    return iReturn
}

