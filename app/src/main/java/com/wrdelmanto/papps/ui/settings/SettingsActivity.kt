package com.wrdelmanto.papps.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.SettingsActivityBinding
import com.wrdelmanto.papps.utils.MAIL_SUBJECT
import com.wrdelmanto.papps.utils.MAIL_TO
import com.wrdelmanto.papps.utils.composeEmail
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.openClearSPDataDialog
import com.wrdelmanto.papps.utils.setupNavigationAndStatusBar
import com.wrdelmanto.papps.utils.showErrorToast

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding

    private val settingsViewModel: SettingsViewModel by viewModels()

    // Toolbar
    private lateinit var settingsArrowBack: ImageView
    private lateinit var countryFlag: ImageView

    // Items
    private lateinit var privacyPolicy: ConstraintLayout
    private lateinit var rateThisApp: ConstraintLayout
    private lateinit var sendFeedback: ConstraintLayout
    private lateinit var clearSPData: ConstraintLayout

    private lateinit var rocket: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle onBackPressed
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                logD { "onBackPressed" }

                finish()
            }
        })

        // Disable dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Adjust navigation and status bar
        setupNavigationAndStatusBar(this, window)

        binding.settingsViewModel = settingsViewModel
        binding.lifecycleOwner = this

        // Toolbar
        settingsArrowBack = binding.settingsActivityToolbarArrowBack
        countryFlag = binding.settingsActivityToolbarCountryFlag

        // Items
        privacyPolicy = binding.settingsActivityPrivacyPocily
        rateThisApp = binding.settingsActivityRateThisApp
        sendFeedback = binding.settingsActivityPappsGooglePlay
        clearSPData = binding.settingsActivityClearData

        rocket = binding.settingsActivityRocketMiniLogo

        initiateListeners()
        initiateObservers()
    }

    override fun onResume() {
        super.onResume()

        settingsViewModel.resetUi(this)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateListeners() {
        // Toolbar
        settingsArrowBack.setOnClickListener { onBackPressed() }
        countryFlag.setOnClickListener { settingsViewModel.switchLanguage(this) }

        // Items
        privacyPolicy.setOnClickListener { openPrivacyPolicy() }
        rateThisApp.setOnClickListener { rateThisApp() }
        sendFeedback.setOnClickListener { sendFeedback() }
        clearSPData.setOnClickListener { openClearSPDataDialog(this) }
    }

    private fun initiateObservers() {
        settingsViewModel.easterEggActivated.observe(this) { easterEggActivated ->
            if (easterEggActivated) rocket.setOnClickListener(null)
            else rocket.setOnClickListener { settingsViewModel.shouldActivateEasterEgg(this) }
        }
    }

    private fun disableListeners() {
        // Toolbar
        settingsArrowBack.setOnClickListener(null)

        // Items
        privacyPolicy.setOnClickListener(null)
        rateThisApp.setOnClickListener(null)
        sendFeedback.setOnClickListener(null)
        clearSPData.setOnClickListener(null)

        rocket.setOnClickListener(null)
    }

    private fun openPrivacyPolicy() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))
        logD { "openPrivacyPolicy" }
    }

    private fun rateThisApp() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PAPPS_GOOGLE_PLAY_URL)))
        logD { "rateThisApp" }
    }

    private fun sendFeedback() {
        try {
            startActivity(
                Intent.createChooser(
                    composeEmail(MAIL_TO, MAIL_SUBJECT),
                    getString(R.string.send_feedback_choose_email_client)
                )
            )
        } catch (e: Exception) {
            showErrorToast(this, R.string.send_feedback_error, Toast.LENGTH_LONG)
        }

        logD { "sendFeedback" }
    }

    private companion object {
        const val PRIVACY_POLICY_URL =
            "https://docs.google.com/document/d/17-S5qZQoqmxN6jkHTWNhW89zdrjVmwkkGNKwauYqPvY"
        const val PAPPS_GOOGLE_PLAY_URL =
            "https://play.google.com/store/apps/details?id=com.wrdelmanto.papps"
    }
}