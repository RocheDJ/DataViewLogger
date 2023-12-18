package ie.djroche.datalogviewer.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ie.djroche.datalogviewer.models.UserManager
import ie.djroche.datalogviewer.models.UserModel

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

   // var AuthManager: UserManager = ie.djroche.datalogviewer.models.UserManager()
    var liveUser: MutableLiveData<UserModel?> = UserManager.liveUser
    var loggedOut: MutableLiveData<Boolean> = UserManager.loggedOut

    fun logOut() {
        UserManager.logOut()
    }
}