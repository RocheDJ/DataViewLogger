package ie.djroche.datalogviewer.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.databinding.ActivityLoginBinding
import ie.djroche.datalogviewer.helpers.ValidateUser
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.UserModel
import timber.log.Timber

lateinit var app: MainApp

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var user : UserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityLoginBinding.inflate(layoutInflater)
        app = application as MainApp
        //set the current logged in username
        val currentUser = app.users.findUserById(app.preferences.getString("UserID","--").toString())
        if (currentUser != null) {
            binding.username.setText(currentUser.email.toString())
            //EditText on Editor Change Listener
            binding.password.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Timber.i(" beforeTextChanged Not yet implemented")
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        if(s.length>4){
                            binding.btnLogin.isEnabled = true
                        } else
                        {
                            binding.btnLogin.isEnabled = false
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    Timber.i(" afterTextChanged Not yet implemented")
                }
        })
        }

        binding.btnLogin.setOnClickListener {
            SignInPressed(it)
        }

        setContentView(binding.root)
    }

private fun textChanged() {

}
//------------------------------------------------------------------------------------------------
    private fun SignInPressed(view :View) {
    try {
        val userEmail = binding.username.text.toString()
        user = app.users.findUserById(userEmail)!!
        val checkPassword = binding.password.toString()
        // if the username is not null  and password length >3
        if ((user != null) && checkPassword.length > 3) {
            val checkResult = ValidateUser(user, checkPassword)
            if (checkResult == 0) {
                app.user=user.copy()
                setResult(RESULT_OK)
                finish()
            } else {
                //ToDo: add option to register user
                Snackbar.make(view, getString(R.string.enter_valid_username_password), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    } catch (e: Exception) {
        Timber.i("SignInPressed ERROR" + e.message)
    }
}
}