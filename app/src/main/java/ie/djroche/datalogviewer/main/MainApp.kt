package ie.djroche.datalogviewer.main
import android.app.Application
import ie.djroche.datalogviewer.models.SiteJSONStore
import ie.djroche.datalogviewer.models.SiteStore
import ie.djroche.datalogviewer.models.UserJSONStore
import ie.djroche.datalogviewer.models.UserStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application()  {
    lateinit var sites : SiteStore
    lateinit var users : UserStore
  //  val sites = SiteMemStore()
    var qrCode : String = ""
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        sites = SiteJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
        i("DataLogViewer started")
    }
}