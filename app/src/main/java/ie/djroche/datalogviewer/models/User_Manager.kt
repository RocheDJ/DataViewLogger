package ie.djroche.datalogviewer.models

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.djroche.datalogviewer.utils.encryptString
import ie.djroche.datalogviewer.utils.exists
import ie.djroche.datalogviewer.utils.loadDummyUserJSONData
import timber.log.Timber
import java.io.File
import java.lang.reflect.Type

//const val JSON_USERFILE = "data/user/0/ie.djroche.datalogviewer/files/users.json"
const val JSON_USERFILE = "users.json"
class User_Manager(application: Application) : UserStore {

    // for JSON
    private var users = mutableListOf<UserModel>()
    private val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting().create()
    private val listType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type
    private var preferences: SharedPreferences

    private var application: Application? = null
    var firebaseAuth: FirebaseAuth? = null

    var liveUser = MutableLiveData<UserModel>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()

    private var isFirebase: Boolean = false

    /*------------------------------------------------------------------------------------------------*/
    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            // convert from firebase user to local type
            var localUser: UserModel = UserModel()
            localUser.email = firebaseAuth!!.currentUser?.email.toString()
            localUser.id = firebaseAuth!!.currentUser?.uid
            localUser.firstName = firebaseAuth!!.currentUser?.displayName.toString()
            localUser.lastName = "Firebase"
            liveUser.postValue(localUser)
            loggedOut.postValue(false)
            errorStatus.postValue(false)
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
        val xFirebase = preferences.getBoolean("FireBase", false)

        if (!xFirebase) {
            deserialize()
        }
        val id = preferences.getString("UserID",null)
        if (id != null){
            var localUser= findUserById(id)
            if (localUser != null){
                liveUser.postValue(localUser!!)
                loggedOut.postValue(false)
                errorStatus.postValue(false)
            }
        }
        if (xFirebase == false) {
            //if user file does not exist create it
            if (!exists(application.applicationContext, JSON_USERFILE)) {
                loadDummyUserJSONData(application.applicationContext)
            }

        }

        isFirebase = xFirebase
    }

    /*------------------------------------------------------------------------------------------------*/
    override fun login(email: String?, password: String?): Boolean {
        var retVal: Boolean = false
        if (isFirebase) {
            firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(application!!.mainExecutor, { task ->
                    if (task.isSuccessful) {
                        var localUser: UserModel = UserModel()
                        localUser.email = firebaseAuth!!.currentUser?.email.toString()
                        localUser.id = firebaseAuth!!.currentUser?.uid
                        localUser.firstName = firebaseAuth!!.currentUser?.displayName.toString()
                        localUser.lastName = "Firebase"
                        liveUser.postValue(localUser)
                        errorStatus.postValue(false)

                        retVal = true
                    } else {
                        Timber.i("Login Failure: $task.exception!!.message")
                        errorStatus.postValue(true)
                    }
                })
        } else {
            // JSON User
            val checkUser = findUserByEmail(email!!)
            if (checkUser!=null){
                //validate
                if (ValidateUser(checkUser!!, password!!) ==0){
                    liveUser.postValue(checkUser!!)
                    loggedOut.postValue(false)

                    SaveCurrentUser(checkUser)
                    retVal = true
                }else{
                    loggedOut.postValue(true)
                }
            }
            Timber.i("Login  JSON user")
        }
        return retVal
    }

    /*------------------------------------------------------------------------------------------------*/
    override fun update(user: UserModel) {

        if (isFirebase != false) {
            var foundUser: UserModel? = users.find { p -> p.id == user.id }
            if (foundUser != null) {
                foundUser.email = user.email
                foundUser.firstName = user.firstName
                foundUser.lastName = user.lastName
                foundUser.password = user.password

                serialize()
            }

        }
    }

    /*------------------------------------------------------------------------------------------------*/
    override fun findAll(): List<UserModel> {
        TODO("Not yet implemented")
    }

    /*------------------------------------------------------------------------------------------------*/
    override fun findUserById(userID: String): UserModel? {
        if (isFirebase) {
            return null
        } else {
            return users.find { p -> p.id == userID }
        }
    }

    /*------------------------------------------------------------------------------------------------*/
    override fun findUserByEmail(eMail: String): UserModel? {
        try {
            if (isFirebase) {
                return null
            } else {
                val foundUser: UserModel? =
                    users.find { p -> p.email == eMail }
                return foundUser
            }
        } catch (e: Exception) {
            timber.log.Timber.e("findUserByEmail Error %s", e.toString())
        }
        return null

    }

    /*------------------------------------------------------------------------------------------------*/
    override fun register(user: UserModel): String {
        if (isFirebase) {
            return ""
        } else {
            users.add(user)
            serialize()
            return user.id!!
        }
    }

    /*------------------------------------------------------------------------------------------------*/
    override fun register(email: String?, password: String?): UserModel? {
        val newUser: UserModel = UserModel()
        try {
            if (isFirebase) {
                firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(application!!.mainExecutor, { task ->
                        if (task.isSuccessful) {
                            newUser.id = firebaseAuth!!.currentUser?.uid
                            newUser.email = firebaseAuth!!.currentUser?.email.toString()
                            newUser.firstName = firebaseAuth!!.currentUser?.displayName.toString()
                            liveUser.postValue(newUser)
                            errorStatus.postValue(false)
                        } else {
                            Timber.i("Registration Failure: $task.exception!!.message")
                            errorStatus.postValue(true)
                        }
                    })
            } else {
                val encryptedPassword: String = encryptString(input = password!!)
                newUser.email = email!!
                newUser.password = encryptedPassword
                users.add(newUser)
                serialize()
                Timber.i("Register  JSON user")
            }
            return newUser
        } catch (e: Exception) {
            timber.log.Timber.e("Register User Error %s", e.toString())
        }
        return null
    }

    /*------------------------------------------------------------------------------------------------*/
    override fun delete(user: UserModel) {
        if (isFirebase != false) {
            users.remove(user)
            serialize()
        }
    }

    /*------------------------------------------------------------------------------------------------*/
    override fun logOut() {
        if (isFirebase){
            firebaseAuth!!.signOut()
        }else
        {
            SavelogoutUser()
        }
        loggedOut.postValue(true)
        errorStatus.postValue(false)

    }

    /*-------------                      JSON Private Functions         -------------------------------*/

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        File(JSON_USERFILE).writeText(jsonString)
    }

    private fun deserialize() {
        try {
            val  path = application?.applicationContext?.filesDir?.absolutePath
            val jsonString = File("$path/$JSON_USERFILE").readText()
            users = gsonBuilder.fromJson(jsonString, listType)
        } catch (e: Exception) {
            Timber.i("User deserialize error : $e.message")
        }

    }

    //--------------------------------------------------------------------------------------------------------------
    fun ValidateUser(user: UserModel, enteredPassword: String): Int {
        var iReturn = -1
        try {
            val encryptedPassword: String = encryptString(input = enteredPassword)
            iReturn = if (encryptedPassword == user.password) {
                0
            } else {
                1
            }
        } catch (e: Exception) {
            timber.log.Timber.e("ValidateUser Error %s", e.toString())
        }
        return iReturn
    }


    fun SaveCurrentUser(user : UserModel){
        var editor = preferences.edit()
        editor.putString("UserID", user.id.toString())
        editor.commit()
        }

    fun SavelogoutUser(){

            var editor = preferences.edit()
            editor.remove("UserID")
            editor.commit()
    }
}