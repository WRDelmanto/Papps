package com.wrdelmanto.papps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.showNormalToast

class HomeFragment : Fragment() {
    private lateinit var homeLogo: ImageView

    private var clicksEasterEgg = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeLogo = view.findViewById(R.id.home_logo)

        initiateListeners()
    }

    private fun initiateListeners() = homeLogo.setOnClickListener { activateEasterEgg() }

    /**
     * Function created to test features.
     */
    private fun activateEasterEgg() {
        clicksEasterEgg++

        if (clicksEasterEgg >= CLICKS_TO_ACTIVATE_EASTER_EGG) {
            homeLogo.setOnClickListener(null)
            context?.let { showNormalToast(it, getString(R.string.easter_egg_activated)) }
            logD { getString(R.string.easter_egg_activated)}
            (activity as MainActivity?)?.activateEasterEgg()
        }
    }

    private companion object {
        const val CLICKS_TO_ACTIVATE_EASTER_EGG = 10
    }
}