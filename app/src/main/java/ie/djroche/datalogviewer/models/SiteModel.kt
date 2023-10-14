package ie.djroche.datalogviewer.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class SiteModel(
            var id : Long =0,
            var qrcode : String = "",
            var description : String ="",
            var updated : Int = 0,
            var modelVersion : Int = 1,
            val data: ArrayList<SiteDataModel>
): Parcelable
