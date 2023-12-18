package ie.djroche.datalogviewer

import android.content.Context
import androidx.test.core.app.ApplicationProvider

import androidx.test.ext.junit.runners.AndroidJUnit4
import ie.djroche.datalogviewer.utils.encryptString

import ie.djroche.datalogviewer.models.UserJSONStore
import ie.djroche.datalogviewer.models.UserModel
import ie.djroche.datalogviewer.models.UserStore

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class userModelTest_Device {
    val context = ApplicationProvider.getApplicationContext<Context>()
    var testUsers : UserStore =UserJSONStore(context)
    var myNewUser_1: UserModel = UserModel(email = "hbart@simpson.com",
        firstName = "bart",
        lastName = "simpson",
        password = encryptString("secret"))
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
        var retId = testUsers.register(myNewUser_1.copy())
        var returnedUser = testUsers.findUserById(retId)
        assertEquals( myNewUser_1,returnedUser)
    }

    @Test
    fun test02_CheckUser_By_Email_Returns_True(){

        var retId = testUsers.register(myNewUser_1.copy())

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
        /* check we can find the user
         then delete the user and no longer find it
         */
        var retId = testUsers.register(myNewUser_1.copy())

        var returnedUser = testUsers.findUserById(retId)
        assertEquals( myNewUser_1,returnedUser)
        testUsers.delete(myNewUser_1)
        var returnedUserPostdelete = testUsers.findUserById(retId)
        assertNull(returnedUserPostdelete)
    }
}