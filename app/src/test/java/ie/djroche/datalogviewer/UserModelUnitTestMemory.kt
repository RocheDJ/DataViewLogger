package ie.djroche.datalogviewer
import ie.djroche.datalogviewer.utils.encryptString
import ie.djroche.datalogviewer.models.UserMemStore
import ie.djroche.datalogviewer.models.UserModel
import ie.djroche.datalogviewer.models.UserStore
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UserModelUnitTestMemory {
    var myNewUser_1: UserModel = UserModel(email = "hbart@simpson.com",
                                         firstName = "bart",
                                        lastName = "simpson",
                                        password = encryptString("secret"))

    var testUsers : UserStore =  UserMemStore()

    @Before
    fun setup() {
        /*
            Setup the dummy user and store
        */
        // setup dummy store
        // load and associate user data
    }


    @Test
    fun test01_CheckUser_By_ID_Return_True(){
        //create a new user and
        var retId = testUsers.create(myNewUser_1.copy())
        var returnedUser = testUsers.findUserById(retId)
        assertEquals( myNewUser_1,returnedUser)

    }

    @Test
    fun test02_CheckUser_By_Email_Returns_True(){
        var retId = testUsers.create(myNewUser_1.copy())

        var returnedUser = testUsers.findUserById(retId)
        assertEquals( myNewUser_1,returnedUser)

        var returnedUserByEmail = testUsers.findUserByEmail(myNewUser_1.email)
        assertEquals(myNewUser_1, returnedUserByEmail)
    }

    @Test
    fun test03_DeleteUser_Returns_False(){
        /* check we can find the user
         then delete the user and no longer find it
         */
        var retId = testUsers.create(myNewUser_1.copy())

        var returnedUser = testUsers.findUserById(retId)
        assertEquals( myNewUser_1,returnedUser)
        testUsers.delete(myNewUser_1)
        var returnedUserPostdelete = testUsers.findUserById(retId)
        assertNull(returnedUserPostdelete)
    }
}