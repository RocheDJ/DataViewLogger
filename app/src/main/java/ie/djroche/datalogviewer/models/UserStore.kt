package ie.djroche.datalogviewer.models

interface UserStore {
    fun update(user: UserModel)
    fun findAll(): List<UserModel>
    fun findUserById(userID: String): UserModel?
    fun findUserByEmail(eMail:String):UserModel?
    fun create(user: UserModel):String
    fun delete(user: UserModel)
}