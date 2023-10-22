package ie.djroche.datalogviewer.main
import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import ie.djroche.datalogviewer.models.SiteJSONStore
import ie.djroche.datalogviewer.models.SiteStore
import ie.djroche.datalogviewer.models.UserJSONStore
import ie.djroche.datalogviewer.models.UserStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application()  {
    lateinit var sites : SiteStore
    lateinit var users : UserStore
    lateinit var httpQueue : RequestQueue // queue of HTTP requests

    var qrCode : String = ""
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        sites = SiteJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
        httpQueue = Volley.newRequestQueue(this)
        i("DataLogViewer started")
    }


}