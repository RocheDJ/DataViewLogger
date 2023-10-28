package ie.djroche.datalogviewer.helpers

import android.content.Context
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.JSON_FILE
import ie.djroche.datalogviewer.models.JSON_USERFILE
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.models.UserModel
import ie.djroche.datalogviewer.models.gsonBuilder
import ie.djroche.datalogviewer.models.listType
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest


fun loadDummyUserJSONData(context: Context) {
    var dummyUsers = mutableListOf<UserModel>()
    var myNewUser_1: UserModel = UserModel()
    var myNewUser_2: UserModel = UserModel()
    try {
        myNewUser_1.email = "homer@simpson.com"
        myNewUser_1.firstName = "homer"
        myNewUser_1.lastName = "simpson"
        myNewUser_1.password = encryptString("secret")

        dummyUsers.add(myNewUser_1.copy())

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
    var id: String
    try {
        // create the site data for 1st site
        myNewSite.description = "Homer Site 001"
        myNewSite.data.add(SiteKPIModel(1, "Temperature", R.drawable.temp, 23.4, "Deg C").copy())
        myNewSite.data.add(SiteKPIModel(1, "Temperature", R.drawable.temp, 23.4, "Deg C").copy())
        myNewSite.data.add(
            SiteKPIModel(
                2,
                "Motor RPM",
                R.drawable.speedometer,
                800.0,
                "RPM"
            ).copy()
        )
        myNewSite.data.add(SiteKPIModel(3, "Valve Status", R.drawable.valve, 0.0, "OFF").copy())
        myNewSite.data.add(SiteKPIModel(4, "Flow Rate", R.drawable.flow, 125.0, "L/m").copy())
        myNewSite.data.add(SiteKPIModel(5, "Tank Level", R.drawable.tank, 65.0, "%").copy())
        myNewSite.qrcode = "QR-000006-000001"
        myNewSite.userid = userID
        dummySites.add(myNewSite.copy())

        // create the site data for 2nd site
        myNewSite1.description = "Homer Site 002"
        myNewSite1.data.add(SiteKPIModel(3, "Temperature", R.drawable.temp, 13.4, "Deg C").copy())
        myNewSite1.data.add(
            SiteKPIModel(
                4,
                "Motor RPM",
                R.drawable.speedometer,
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

