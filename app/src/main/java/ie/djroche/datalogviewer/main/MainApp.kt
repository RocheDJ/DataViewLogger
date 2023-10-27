package ie.djroche.datalogviewer.main
import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

import ie.djroche.datalogviewer.models.SiteJSONStore
import ie.djroche.datalogviewer.models.SiteStore
import ie.djroche.datalogviewer.models.SiteWEBStore
import ie.djroche.datalogviewer.models.UserJSONStore
import ie.djroche.datalogviewer.models.UserStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application()  {
    lateinit var sites : SiteStore
    lateinit var users : UserStore
    lateinit var httpQueue : RequestQueue // queue of HTTP requests
    lateinit var preferences: SharedPreferences // ref https://www.digitalocean.com/community/tutorials/android-sharedpreferences-kotlin
    var qrCode : String = ""

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        preferences = getDefaultSharedPreferences(applicationContext)
       // check if we are using JSON Store or API
        val UseJSON :Boolean = true //  preferences.getBoolean("JSONData",true)
        if (UseJSON){
            // load and associate data
            users = UserJSONStore(applicationContext)
            // get the default username is not set then create it from user file
            val defaultUser = users.findUserByEmail("homer@simpson.com")
            if(preferences.getString("UserID","-") =="-"){
                var editor = preferences.edit()
                if (defaultUser != null) {
                    editor.putString("UserID",defaultUser.id)
                }
                    editor.commit()
            }
            if (defaultUser != null) {
                sites = SiteJSONStore(applicationContext,defaultUser.id)
            }
        } else {
            sites = SiteWEBStore(applicationContext)
           //ToDo: make Users web store placeholder  users = UserWebStore(applicationContext)
           //ToDo: make sites web store placeholder  users = UserWebStore(applicationContext)
        }



        httpQueue = Volley.newRequestQueue(this)
        i("DataLogViewer started")
    }


}