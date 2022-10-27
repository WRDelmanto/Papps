package com.wrdelmanto.papps.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commitNow
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.setupNavigationAndStatusBar

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_empty_activity)

        // Disable dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setupNavigationAndStatusBar(applicationContext, window)

        // Load settings fragment
        supportFragmentManager.commitNow { replace(R.id.settings_activity_container, SettingsFragment()) }
    }
}