package ie.djroche.datalogviewer.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class SiteModel(
    var id : String =  UUID.randomUUID().toString(),
    var qrcode : String = "",
    var description : String ="",
    var updated : Int = 0,
    var modelVersion : Int = 1,
    var userid : String = "",
    var data : MutableList<SiteKPIModel> = mutableListOf<SiteKPIModel>()
): Parcelable
