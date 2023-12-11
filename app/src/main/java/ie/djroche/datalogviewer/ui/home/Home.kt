package ie.djroche.datalogviewer.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.databinding.HomeBinding
import ie.djroche.datalogviewer.databinding.NavHeaderBinding
import ie.djroche.datalogviewer.main.MainApp

class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navViewHeaderBinding: NavHeaderBinding
    private lateinit var homeBinding: HomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        drawerLayout = homeBinding.drawerLayout

        val toolbar = findViewById<Toolbar>(R.id.toolbar) // in app_bar_home
        setSupportActionBar(toolbar)



    }
}