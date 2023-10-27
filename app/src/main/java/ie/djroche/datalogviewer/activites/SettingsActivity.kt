package ie.djroche.datalogviewer.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import ie.djroche.datalogviewer.R

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fcTitle,SettingsTitleFragment())
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