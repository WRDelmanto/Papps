package com.wrdelmanto.papps.apps.nasaPictureOfTheDay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commitNow
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.setupNavigationAndStatusBarFullScreen

class NasaPictureOfTheDayHdurlActivity : AppCompatActivity() {
    lateinit var hdurl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_activity)

        // Get extra
        hdurl = intent.extras?.getString("hdurl").toString()

        // Load fragment
        supportFragmentManager.commitNow {
            replace(
                R.id.empty_activity_container, NasaPictureOfTheDayHdurlFragment(applicationContext)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        // Disable dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Adjust navigation and status bar
        setupNavigationAndStatusBarFullScreen(applicationContext, window)
    }
}