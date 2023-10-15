package ie.djroche.datalogviewer.main
import android.app.Application
import ie.djroche.datalogviewer.models.SiteJSONStore
import ie.djroche.datalogviewer.models.SiteMemStore
import ie.djroche.datalogviewer.models.SiteStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application()  {
    lateinit var sites : SiteStore
  //  val sites = SiteMemStore()
    var qrCode : String = ""
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        sites = SiteJSONStore(applicationContext)
        i("DataLogViewer started")
    }
}