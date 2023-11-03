package ie.djroche.datalogviewer.models

class UserMemStore( ):UserStore
{
    var users = mutableListOf<UserModel>()


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
        return user.id.toString()
    }

    override fun delete(user: UserModel) {
        users.remove(user)
    }

}
