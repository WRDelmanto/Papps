package com.wrdelmanto.papps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.CLICK_COUNTER_REF
import com.wrdelmanto.papps.utils.SP_EASTER_EGG
import com.wrdelmanto.papps.utils.checkKeySharedPreferences
import com.wrdelmanto.papps.utils.getFirebase
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putFirebase
import com.wrdelmanto.papps.utils.putSharedPreferences
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
    }

    override fun onResume() {
        super.onResume()

        resetUi()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() = homeLogo.setOnClickListener {
        shouldActivateEasterEgg()
        test()
    }

    private fun disableListeners() {
        homeLogo.apply {
            setOnClickListener(null)
            setOnClickListener { test() }
        }
    }

    private fun resetUi() {
        (activity as MainActivity?)?.updateAppBarTitle("")

        if (context?.let { checkKeySharedPreferences(it, SP_EASTER_EGG) } == true) {
            if (!(context?.let { getSharedPreferences(it, SP_EASTER_EGG, Boolean) } as Boolean)) initiateListeners()
            else disableListeners()
        } else initiateListeners()
    }

    private fun shouldActivateEasterEgg() {
        clicksEasterEgg++
        if (clicksEasterEgg >= CLICKS_TO_ACTIVATE_EASTER_EGG) {
            disableListeners()
            (activity as MainActivity?)?.activateEasterEgg()
            context?.let { putSharedPreferences(it, SP_EASTER_EGG, true) }
            context?.let { showNormalToast(it, getString(R.string.easter_egg_activated)) }
            logD { getString(R.string.easter_egg_activated)}
        }
    }

    /**
     * Function created to test features.
     */
    @Suppress("EmptyMethod")
    private fun test() {
        val score = ("1".toInt().."100".toInt()).random()

//        putFirebase(score.toString(), CLICK_COUNTER_REF)
        context?.let { showNormalToast(it, getFirebase(CLICK_COUNTER_REF, "Test").toString()) }
    }

    private companion object {
        const val CLICKS_TO_ACTIVATE_EASTER_EGG = 10
    }
}