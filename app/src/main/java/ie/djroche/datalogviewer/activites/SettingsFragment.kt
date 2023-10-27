package ie.djroche.datalogviewer.activites

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ie.djroche.datalogviewer.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}