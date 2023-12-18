package ie.djroche.datalogviewer.models

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.djroche.datalogviewer.activities.app
import ie.djroche.datalogviewer.utils.encryptString
import timber.log.Timber
import java.io.File
import java.lang.reflect.Type

object UserManager:UserStore {
    const val JSON_USERFILE = "data/user/0/ie.djroche.datalogviewer/files/users.json"
    private var users = mutableListOf<UserModel>()
    private val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting().create()
    private val listType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type
    lateinit var preferences: SharedPreferences

    var liveUser = MutableLiveData<UserModel?>()
    var loggedOut = MutableLiveData<Boolean>()

    init {
        try {
            loggedOut.postValue(true)
            preferences = PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
            // load the last user
            val lastUser : UserModel? = findUserById(preferences.getString("UserID","--").toString())

            liveUser.value = lastUser!!
        } catch ( e :Exception){
            Timber.i("UserManager Init Error " + e.message)
        }

    }

    override fun update(user: UserModel) {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<UserModel> {
        TODO("Not yet implemented")
    }

    override fun findUserById(userID: String): UserModel? {
        deserialize()
        return users.find { p -> p.id == userID }
    }

    override fun findUserByEmail(eMail: String): UserModel? {
        try {
            deserialize()
            val foundUser: UserModel? =
                users.find{ p -> p.email == eMail }
            return foundUser
        } catch (e: Exception) {
            timber.log.Timber.e("findUserByEmail Error %s", e.toString())
        }
        return null
    }
    override fun register(user:UserModel): String {
        deserialize()
       users.add(user)
       serialize()
        return user.id!!
    }

    override fun register(email: String?, password: String?): UserModel? {
        deserialize()
        val newUser :UserModel = UserModel()
        newUser.email = email!!
        newUser.password = password!! // ToDo:add encryption
        users.add(newUser)
        serialize()
        return newUser
    }

    override fun delete(user: UserModel) {
        TODO("Not yet implemented")
    }

    override fun login(email: String?, password: String?) :Boolean{
        // find by email then validate
        val checkUser = findUserByEmail(email!!)
        if (checkUser!=null){
            //validate
            if (ValidateUser(checkUser!!,password!!) ==0){
                liveUser.postValue(checkUser!!)
                loggedOut.postValue(false)
            }else{
                loggedOut.postValue(true)
            }
        }

        return !(loggedOut.value!!)
    }

    override fun logOut() {
        loggedOut.postValue(true)

        liveUser.postValue(null)
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        File( JSON_USERFILE).writeText(jsonString)
    }

    private fun deserialize() {
        try{
            val jsonString = File(JSON_USERFILE).readText()
            users = gsonBuilder.fromJson(jsonString, listType)
        }catch(e:Exception){
            Timber.i("User deserialize error : $e.message")
        }

    }

    //--------------------------------------------------------------------------------------------------------------
    fun ValidateUser(user :UserModel, enteredPassword :String) : Int{
        var iReturn =-1
        try {
            val encryptedPassword :String = encryptString(input=enteredPassword)
            iReturn = if (encryptedPassword == user.password){
                0
            }else{
                1
            }
        }catch (e: Exception){
            timber.log.Timber.e("ValidateUser Error %s", e.toString())
        }
        return iReturn
    }

}