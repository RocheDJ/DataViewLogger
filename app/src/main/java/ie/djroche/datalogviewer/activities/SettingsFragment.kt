package ie.djroche.datalogviewer.activities

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ie.djroche.datalogviewer.R

class SettingsFragment : PreferenceFragmentCompat() {
    // see ref https://developer.android.com/guide/fragments/communicate
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}