
package ie.djroche.datalogviewer.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class UserModel (
    var id          :   String? = UUID.randomUUID().toString(), // allow id to be nullable receiver
                                                                //https://kotlinlang.org/docs/null-safety.html#nullable-receiver
    var firstName   :   String="",
    var lastName    :   String="",
    var email       :   String="",
    var password    :   String="",
): Parcelable