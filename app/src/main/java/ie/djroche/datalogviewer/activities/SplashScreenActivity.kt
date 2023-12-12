package ie.djroche.datalogviewer.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ie.djroche.datalogviewer.ui.home.Home

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start the main activity after a delay
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
               // startActivity(Intent(this, MainActivity::class.java))
                startActivity(Intent(this, Home::class.java))
                finish()
            }, 2000)
        }
    }
}