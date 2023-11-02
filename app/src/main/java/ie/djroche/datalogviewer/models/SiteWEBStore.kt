package ie.djroche.datalogviewer.models
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import timber.log.Timber
import java.lang.reflect.Type
import ie.djroche.datalogviewer.helpers.*
import android.content.Context

class SiteWEBStore(private val context: Context) : SiteStore {

    var sites = mutableListOf<SiteModel>()
    override fun update(site: SiteModel) {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<SiteModel> {
        TODO("Not yet implemented need to have validated KEY for Server")
    }

    override fun findAllForUser(userID: String): List<SiteModel> {
        TODO("Not yet implemented")
    }

    override fun find(siteID: String): SiteModel? {
        TODO("Not yet implemented")
    }

    override fun findByQR(QRCode: String): SiteModel? {
        TODO("Not yet implemented")
    }

    override fun create(site: SiteModel): String {
        TODO("Not yet implemented")
    }

    override fun delete(site: SiteModel) {
        TODO("Not yet implemented")
    }

    override fun getkpi(siteID: String): MutableList<SiteKPIModel>? {
        TODO("Not yet implemented")
    }


}