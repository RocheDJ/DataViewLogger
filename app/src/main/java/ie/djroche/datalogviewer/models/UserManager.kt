package ie.djroche.datalogviewer.models

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import timber.log.Timber
import java.io.File

object UserManager:UserStore {
    const val JSON_USERFILE = "data/user/0/ie.djroche.datalogviewer/files/users.json"
    private var users = mutableListOf<UserModel>()
    private val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
        .create()

    override fun update(user: UserModel) {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<UserModel> {
        TODO("Not yet implemented")
    }

    override fun findUserById(userID: String): UserModel? {
        TODO("Not yet implemented")
    }

    override fun findUserByEmail(eMail: String): UserModel? {
        TODO("Not yet implemented")
    }

    override fun create(user: UserModel): String {
        TODO("Not yet implemented")
    }

    override fun delete(user: UserModel) {
        TODO("Not yet implemented")
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        File( JSON_USERFILE).writeText(jsonString)
    }

    private fun deserialize() {
        try{
            val jsonString = File(UserManager.JSON_USERFILE).readText()
            users = gsonBuilder.fromJson(jsonString, listType)
        }catch(e:Exception){
            Timber.i("User deserialize error : $e.message")
        }

    }
}