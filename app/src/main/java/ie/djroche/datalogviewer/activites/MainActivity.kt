package ie.djroche.datalogviewer.activites

//ToDo:Add Main App

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.djroche.datalogviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // -----------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}