package ie.djroche.datalogviewer.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.auth.LoggedInViewModel
import ie.djroche.datalogviewer.auth.Login
import ie.djroche.datalogviewer.databinding.HomeBinding
import ie.djroche.datalogviewer.databinding.NavHeaderBinding
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.models.UserModel
import ie.djroche.datalogviewer.ui.site.SiteViewModel
import timber.log.Timber


class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navHeaderBinding: NavHeaderBinding
    private lateinit var homeBinding: HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel: LoggedInViewModel

    // site view model to enable showing selected site on nav drawer
    private lateinit var siteViewModel: SiteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = HomeBinding.inflate(layoutInflater)

        setContentView(homeBinding.root)

        drawerLayout = homeBinding.drawerLayout

        val toolbar = findViewById<Toolbar>(R.id.toolbar) // in app_bar_home
        setSupportActionBar(toolbar)
        toolbar.title = title
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.aboutFragment, R.id.detailFragment, R.id.siteFragment,
                R.id.kpiFragment
            ), drawerLayout
        )


        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)


        Timber.i("Home Activity created...")
    }

    /* ----------------------------------------------------------------------------------------------- */
    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)

        siteViewModel = ViewModelProvider(this).get(SiteViewModel::class.java)
        // if the logged in user changes update the nave header
        loggedInViewModel.liveUser.observe(this, Observer { liveUser ->
            if (liveUser != null)
                updateNavHeader(loggedInViewModel.liveUser.value!!)
        })

        // if the live data changes to logged out then open the login dialogue
        loggedInViewModel.loggedOut.observe(this, Observer { userLoggedOut ->
            if (userLoggedOut) {
                startActivity(Intent(this, Login::class.java))
            }
        })
        Timber.i("Home Activity started...")
    }

    /* ----------------------------------------------------------------------------------------------- */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        Timber.i("Home Activity Navigate Up...")
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onRestart() {
        super.onRestart()

        siteViewModel = ViewModelProvider(this).get(SiteViewModel::class.java)
        // if the selected site changes the update the nav drawer
        siteViewModel.observableSite.observe(this, Observer { liveSite ->
            if (liveSite != null)
                updateNavHeader_Site(siteViewModel.liveSite.value!!)
        })

    }

    /* ----------------------------------------------------------------------------------------------- */
    private fun updateNavHeader(currentUser: UserModel) {
        val headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.tvUser.text = currentUser.email

    }

    /* ----------------------------------------------------------------------------------------------- */
    private fun updateNavHeader_Site(currentSite: SiteModel) {
        val headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        navHeaderBinding.tvSite.text = currentSite.description
    }

    private fun signOut() {
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    //-------------------- signOut called from nav_drawer_menu.xml
    fun signOut(item: MenuItem) {
        signOut()
    }
}