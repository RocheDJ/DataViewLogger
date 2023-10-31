package ie.djroche.datalogviewer.activities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.main.MainApp

class SettingsActivity : AppCompatActivity() {
    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //launch the main app
        app = application as MainApp
        //set the user name on the settings fragment
        val mySettingsTitle = SettingsTitleFragment()
        mySettingsTitle.UserName= app.user.firstName + " " + app.user.lastName

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fcTitle,mySettingsTitle)
                .replace(R.id.fcSettings, SettingsFragment())
                .commit()
        }
        setContentView(R.layout.settings_activity)
    }

    //press back on settings return ok and finish
    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK);
        finish()
    }
}