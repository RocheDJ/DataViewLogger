package ie.djroche.datalogviewer.main
import android.app.Application
import ie.djroche.datalogviewer.models.SiteMemStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application()  {

    val sites = SiteMemStore()
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("DataLogViewer started")
    }

}