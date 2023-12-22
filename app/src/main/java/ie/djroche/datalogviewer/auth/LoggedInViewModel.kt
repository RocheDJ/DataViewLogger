package ie.djroche.datalogviewer.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ie.djroche.datalogviewer.models.UserModel
import ie.djroche.datalogviewer.models.User_Manager

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    var user_Manager : User_Manager = User_Manager(app)
    var liveUser: MutableLiveData<UserModel> = user_Manager.liveUser
    var loggedOut: MutableLiveData<Boolean> = user_Manager.loggedOut

    fun logOut() {
        user_Manager.logOut()
    }
}