package com.wrdelmanto.papps.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.openClearSPDataDialog

class SettingsFragment : Fragment() {
    // Toolbar
    private lateinit var settingsArrowBack: ImageView

    // Items
    private lateinit var privacyPolicy: ConstraintLayout
    private lateinit var clearSPData: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar
        settingsArrowBack = view.findViewById(R.id.settings_toolbar_arrow_back)

        // Items
        privacyPolicy = view.findViewById(R.id.privacy_pocily)
        clearSPData = view.findViewById(R.id.clear_data)
    }

    override fun onResume() {
        super.onResume()

        initiateListeners()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    @Suppress("DEPRECATION")
    private fun initiateListeners() {
        // Toolbar
        settingsArrowBack.setOnClickListener { activity?.onBackPressed() }

        // Items
        privacyPolicy.setOnClickListener { openPrivacyPolicy() }
        clearSPData.setOnClickListener { context?.let { openClearSPDataDialog(it) } }
    }

    private fun disableListeners() {
        // Toolbar
        settingsArrowBack.setOnClickListener(null)

        // Items
        privacyPolicy.setOnClickListener(null)
        clearSPData.setOnClickListener(null)
    }

    /**
     * Show Privacy Policy Screen
     */
    private fun openPrivacyPolicy() =
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))

    private companion object {
        /**
         * Privacy Policy URL
         */
        const val PRIVACY_POLICY_URL =
            "https://docs.google.com/document/d/17-S5qZQoqmxN6jkHTWNhW89zdrjVmwkkGNKwauYqPvY"
    }
}