package ie.djroche.datalogviewer.models
import android.media.Image
import android.net.Uri
import android.os.Parcelable
import ie.djroche.datalogviewer.R
import kotlinx.parcelize.Parcelize

private val defaultImage =(R.drawable.industry40);
//Data Model to hold Key Performance Indicators
@Parcelize
data class SiteKPIModel(
    val id      :   Int     =   0,
    val title   :   String  =   "",
    val icon    :   Int     =   defaultImage,
    val value   :   Double  =   0.0,
    val unit    :   String  =   "-"
): Parcelable
