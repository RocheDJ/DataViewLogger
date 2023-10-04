package ie.djroche.datalogviewer.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class SiteDataModel(
    val id:Int =0,
    val title : String = "",
    val icon :Int = 0,
    val value : Double =0.0,
    val unit : String= "-"
): Parcelable
