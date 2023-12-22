package ie.djroche.datalogviewer.main
//-------------------------------------------------------------------------------------------------
import android.app.Application
import ie.djroche.datalogviewer.models.Site_Manager

import timber.log.Timber
import timber.log.Timber.Forest.i

//-------------------------------------------------------------------------------------------------
class MainApp : Application()  {

//-------------------------------------------------------------------------------------------------
    lateinit var site_Manager : Site_Manager
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        site_Manager = Site_Manager(this)
        i("DataLogViewer started")
    }

} // ------------------------------END Of Class ---------------------------------------------------