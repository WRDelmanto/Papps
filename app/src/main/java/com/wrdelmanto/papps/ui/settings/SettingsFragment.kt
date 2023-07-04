package com.wrdelmanto.papps.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast.LENGTH_LONG
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.BuildConfig
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.SettingsFragmentBinding
import com.wrdelmanto.papps.utils.MAIL_SUBJECT
import com.wrdelmanto.papps.utils.MAIL_TO
import com.wrdelmanto.papps.utils.SP_EASTER_EGG
import com.wrdelmanto.papps.utils.checkKeySharedPreferences
import com.wrdelmanto.papps.utils.composeEmail
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.openClearSPDataDialog
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.showErrorToast
import com.wrdelmanto.papps.utils.showNormalToast

class SettingsFragment : Fragment() {
    private lateinit var binding: SettingsFragmentBinding

    // Toolbar
    private lateinit var settingsArrowBack: ImageView

    // Items
    private lateinit var privacyPolicy: ConstraintLayout
    private lateinit var rateThisApp: ConstraintLayout
    private lateinit var sendFeedback: ConstraintLayout
    private lateinit var clearSPData: ConstraintLayout

    private lateinit var rocket: ImageView
    private lateinit var version: TextView

    private var clicksEasterEgg = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar
        settingsArrowBack = binding.settingsFragmentToolbarArrowBack

        // Items
        privacyPolicy = binding.settingsFragmentPrivacyPocily
        rateThisApp = binding.settingsFragmentRateThisApp
        sendFeedback = binding.settingsFragmentPappsGooglePlay
        clearSPData = binding.settingsFragmentClearData

        rocket = binding.settingsFragmentRocketMiniLogo
        version = binding.settingsFragmentVersion
        version.text = BuildConfig.VERSION_NAME
    }

    override fun onResume() {
        super.onResume()

        initiateListeners()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() {
        // Toolbar
        settingsArrowBack.setOnClickListener { activity?.onBackPressed() }

        // Items
        privacyPolicy.setOnClickListener { openPrivacyPolicy() }
        rateThisApp.setOnClickListener { rateThisApp() }
        sendFeedback.setOnClickListener { sendFeedback() }
        clearSPData.setOnClickListener { context?.let { openClearSPDataDialog(it) } }

        rocket.setOnClickListener { shouldActivateEasterEgg() }
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

    private fun openPrivacyPolicy() =
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))

    private fun rateThisApp() =
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PAPPS_GOOGLE_PLAY_URL)))

    private fun sendFeedback() {
        try {
            startActivity(
                Intent.createChooser(
                    composeEmail(MAIL_TO, MAIL_SUBJECT),
                    getString(R.string.send_feedback_choose_email_client)
                )
            )
        } catch (e: Exception) {
            context?.let { showErrorToast(it, R.string.send_feedback_error, LENGTH_LONG) }
        }
    }

    private fun shouldActivateEasterEgg() {
        clicksEasterEgg++

        if (clicksEasterEgg >= CLICKS_TO_ACTIVATE_EASTER_EGG) {
            clicksEasterEgg = 0

            if (context?.let { checkKeySharedPreferences(it, SP_EASTER_EGG) } == true) {
                if (context?.let {
                        getSharedPreferences(
                            it, SP_EASTER_EGG, Boolean
                        ) as Boolean
                    } == true) {
                    context?.let {
                        showNormalToast(
                            it, getString(R.string.easter_egg_already_activated)
                        )
                    }
                    logD { getString(R.string.easter_egg_already_activated) }
                } else {
                    context?.let { putSharedPreferences(it, SP_EASTER_EGG, true) }
                    context?.let { showNormalToast(it, getString(R.string.easter_egg_activated)) }
                    logD { getString(R.string.easter_egg_activated) }
                }
            } else {
                context?.let { putSharedPreferences(it, SP_EASTER_EGG, true) }
                context?.let { showNormalToast(it, getString(R.string.easter_egg_activated)) }
                logD { getString(R.string.easter_egg_activated) }
            }
        }
    }

    private companion object {
        /**
         * Privacy Policy URL
         */
        const val PRIVACY_POLICY_URL =
            "https://docs.google.com/document/d/17-S5qZQoqmxN6jkHTWNhW89zdrjVmwkkGNKwauYqPvY"

        /**
         * Papps Google Play URL
         */
        const val PAPPS_GOOGLE_PLAY_URL =
            "https://play.google.com/store/apps/details?id=com.wrdelmanto.papps"

        const val CLICKS_TO_ACTIVATE_EASTER_EGG = 5
    }
}