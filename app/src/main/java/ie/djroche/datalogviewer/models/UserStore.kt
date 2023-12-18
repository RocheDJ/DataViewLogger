package ie.djroche.datalogviewer.models

interface UserStore {
    fun update(user: UserModel)
    fun findAll(): List<UserModel>
    fun findUserById(userID: String): UserModel?
    fun findUserByEmail(eMail:String):UserModel?
    fun register(user: UserModel):String
    fun register(email: String?, password: String?) :UserModel?
    fun delete(user: UserModel)
    fun login(email: String?, password: String?):Boolean

    fun logOut()
}