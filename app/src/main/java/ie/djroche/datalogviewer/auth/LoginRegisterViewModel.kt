package ie.djroche.datalogviewer.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ie.djroche.datalogviewer.models.UserManager
import ie.djroche.datalogviewer.models.UserModel

class LoginRegisterViewModel (app: Application) : AndroidViewModel(app) {

    var liveUser: MutableLiveData<UserModel?> = UserManager.liveUser
    var loggedOut: MutableLiveData<Boolean> = UserManager.loggedOut
    fun login(email: String?, password: String?) {
        UserManager.login(email, password)
    }

    fun register(email: String?, password: String?) {
        UserManager.register(email, password)
    }

}