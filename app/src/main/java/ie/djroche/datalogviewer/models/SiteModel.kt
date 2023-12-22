package ie.djroche.datalogviewer.models
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import java.util.UUID
@IgnoreExtraProperties
@Parcelize
data class SiteModel(
    var id              : String =  UUID.randomUUID().toString(),
    var qrcode          : String = UUID.randomUUID().toString(),
    var description     : String = "",
    var updated         : Int    = 0,
    var modelVersion    : Int    = 1,
    var userid          : String = "",
    var data            : MutableList<SiteKPIModel> = mutableListOf<SiteKPIModel>()
): Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to id,
            "qrcode" to qrcode,
            "description" to description,
            "updated" to updated,
            "modelVersion" to modelVersion,
            "userid" to userid,
            "data" to data
        )
    }
}