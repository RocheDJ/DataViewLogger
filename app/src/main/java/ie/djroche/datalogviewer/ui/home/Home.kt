package ie.djroche.datalogviewer.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.databinding.HomeBinding
import ie.djroche.datalogviewer.databinding.NavHeaderBinding
import timber.log.Timber

class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navViewHeaderBinding: NavHeaderBinding
    private lateinit var homeBinding: HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = HomeBinding.inflate(layoutInflater)

        setContentView(homeBinding.root)

        drawerLayout = homeBinding.drawerLayout

        val toolbar = findViewById<Toolbar>(R.id.toolbar) // in app_bar_home
        setSupportActionBar(toolbar)
        toolbar.title = title

        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.aboutFragment, R.id.detailFragment, R.id.siteFragment,
            R.id.kpiFragment), drawerLayout)


        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

        Timber.i("Home Activity created...")
    }

    /* ----------------------------------------------------------------------------------------------- */
    public override fun onStart() {
        super.onStart()
        Timber.i("Home Activity started...")
    }
    /* ----------------------------------------------------------------------------------------------- */
    override fun onSupportNavigateUp(): Boolean {

        val navController = findNavController(R.id.nav_host_fragment)
        Timber.i("Home Activity Navigate Up...")
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }



}