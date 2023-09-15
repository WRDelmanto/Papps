package com.wrdelmanto.papps.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.SP_EASTER_EGG
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.showNormalToast

class SettingsViewModel : ViewModel() {
    private val _easterEggActivated = MutableLiveData<Boolean>()
    val easterEggActivated: LiveData<Boolean> = _easterEggActivated

    private var easterEggCounter = 0

    fun resetUi(context: Context) {
        _easterEggActivated.value = SP_EASTER_EGG.let {
            val eg = getSharedPreferences(context, it, Boolean)
            eg ?: false
        }.toString().toBoolean()

        logD { "resetUi" }
    }

    fun shouldActivateEasterEgg(context: Context) {
        easterEggCounter++

        if (easterEggCounter >= CLICKS_TO_ACTIVATE_EASTER_EGG && _easterEggActivated.value == false) {
            easterEggCounter = 0

            _easterEggActivated.value = true

            putSharedPreferences(context, SP_EASTER_EGG, true)
            showNormalToast(context, context.resources.getString(R.string.easter_egg_activated))
            logD { context.resources.getString(R.string.easter_egg_activated) }
        }
    }

    private companion object {
        const val CLICKS_TO_ACTIVATE_EASTER_EGG = 5
    }
}