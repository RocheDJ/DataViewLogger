package ie.djroche.datalogviewer.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.djroche.datalogviewer.helpers.exists
import ie.djroche.datalogviewer.helpers.loadDummyUserJSONData
import ie.djroche.datalogviewer.helpers.read
import ie.djroche.datalogviewer.helpers.write
import java.lang.reflect.Type

const val JSON_USERFILE = "users.json"

class UserJSONStore( private val context: Context):UserStore
{

    val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
        .create()
    val listType: Type = object : TypeToken<ArrayList<UserModel>>() {}.type
    var users = mutableListOf<UserModel>()

    // if user file exists read the data from it
    init {
        //if user file does not exist create it
        if (exists(context, JSON_USERFILE)) {
            deserialize()
        } else{
            loadDummyUserJSONData(context)
            deserialize()
        }
    }

    // Update User data
    override fun update(user: UserModel) {
        TODO("Not yet implemented")
    }

    // FindAll returns all users
    override fun findAll(): List<UserModel> {
        return users
    }

    // find a user based on ID
    override fun findUserById(userID: String): UserModel? {
        var foundUser: UserModel? = users.find { p -> p.id == userID }
        return foundUser
    }

    override fun findUserByEmail(email: String): UserModel? {
        var foundUser: UserModel? = users.find { p -> p.email == email }
        return foundUser
    }

    // Add a user to the data array
    override fun create(user: UserModel): String {
        users.add(user.copy())
        serialize()
        return user.id.toString()
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }

    //convert user Data to JSON and save
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_USERFILE, jsonString)
    }

    // Read the data direct to JSON
    private fun deserialize() {
        val jsonString = read(context, JSON_USERFILE)
        users = gsonBuilder.fromJson(jsonString, listType)
    }

}
