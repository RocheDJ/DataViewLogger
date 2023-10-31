package ie.djroche.datalogviewer.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import ie.djroche.datalogviewer.R



class SettingsTitleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val retVal =inflater.inflate(R.layout.fragment_settings_title, container, false)

        var text : TextView = retVal.findViewById(R.id.tvTitle)
        text.setText(getString(R.string.app_name))

        text = retVal.findViewById(R.id.tvUser)
        val userText : String = getString(R.string.user_name) + " Homer Simpson" //ToDo: add username variable
        text.setText(userText)

        return retVal
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            SettingsTitleFragment().apply {
            }
    }

}