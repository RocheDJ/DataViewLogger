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
import ie.djroche.datalogviewer.models.UserModel
import ie.djroche.datalogviewer.models.UserStore
import timber.log.Timber
import timber.log.Timber.Forest.i
import kotlin.properties.Delegates

class MainApp : Application()  {
    lateinit var sites : SiteStore
    lateinit var users : UserStore
    lateinit var httpQueue : RequestQueue // queue of HTTP requests
    lateinit var preferences: SharedPreferences // ref https://www.digitalocean.com/community/tutorials/android-sharedpreferences-kotlin
    lateinit var user : UserModel // active user
    var xLoggedin by Delegates.notNull<Boolean>()
    var qrCode : String = ""

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        preferences = getDefaultSharedPreferences(applicationContext)
       // check if we are using JSON Store or API
        xLoggedin =false
        // load and associate user data
        users = UserJSONStore(applicationContext)

        // get the logged in user username is not set then create it from user file
        val id = preferences.getString("UserID",null)
        if (id != null){
            //read the user details for the user
            user = users.findUserById(id)!!
        } else {
            // if no user defined use a defalt user
            user = users.findUserByEmail("homer@simpson.com")!!
            var editor = preferences.edit()
            if (user != null) {
                editor.putString("UserID",user.id)
            }
            editor.commit()
        }
        LoadSitesForCurrentUser()

        // create the http queue
        httpQueue = Volley.newRequestQueue(this)
        i("DataLogViewer started")
    }
//load sites
    fun LoadSitesForCurrentUser(){
     // load the sites for the user
        if (user != null) {
            sites = SiteJSONStore(applicationContext,user.id)
        }
    }

}