package com.wrdelmanto.papps.ui.settings

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.BuildConfig
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.SP_CURRENT_LANGUAGE
import com.wrdelmanto.papps.utils.SP_EASTER_EGG
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.logD
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.showNormalToast

class SettingsViewModel : ViewModel() {
    private val _easterEggActivated = MutableLiveData<Boolean>()
    val easterEggActivated: LiveData<Boolean> = _easterEggActivated

    private val _contryFlag = MutableLiveData<Drawable>()
    val contryFlag: LiveData<Drawable> = _contryFlag

    val version = BuildConfig.VERSION_NAME

    private lateinit var currentLanguage: String

    private var easterEggCounter = 0

    fun resetUi(context: Context) {
        _easterEggActivated.value = SP_EASTER_EGG.let {
            val eg = getSharedPreferences(context, it, Boolean)
            eg ?: false
        }.toString().toBoolean()

        currentLanguage = SP_CURRENT_LANGUAGE.let {
            val eg = getSharedPreferences(context, it, String)
            eg ?: EN_US_LANGUAGE
        }.toString()

        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(currentLanguage))

        updateFlag(context)

        logD { "resetUi" }
    }

    fun switchLanguage(context: Context) {
        currentLanguage = when (currentLanguage) {
            EN_US_LANGUAGE -> PT_BR_LANGUAGE
            PT_BR_LANGUAGE -> EN_US_LANGUAGE
            else -> EN_US_LANGUAGE
        }

        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(currentLanguage))

        updateFlag(context)

        putSharedPreferences(context, SP_CURRENT_LANGUAGE, currentLanguage)

        logD { "Current language=$currentLanguage" }
    }

    private fun updateFlag(context: Context) {
        _contryFlag.value = when (currentLanguage) {
            EN_US_LANGUAGE -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.flag_united_states, null
            )

            PT_BR_LANGUAGE -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.flag_brazil, null
            )

            else -> {
                null
            }
        }
    }

    fun shouldActivateEasterEgg(context: Context) {
        easterEggCounter++

        if (_easterEggActivated.value == false) {
            if (easterEggCounter >= CLICKS_TO_ACTIVATE_EASTER_EGG) {
                easterEggCounter = 0

                _easterEggActivated.value = true

                putSharedPreferences(context, SP_EASTER_EGG, true)
                showNormalToast(context, context.resources.getString(R.string.easter_egg_activated))
                logD { context.resources.getString(R.string.easter_egg_activated) }
            } else {
                val clickLeft = CLICKS_TO_ACTIVATE_EASTER_EGG - easterEggCounter
                logD { "Clicks to activate easter egg=$clickLeft" }
            }
        } else {
            logD { context.resources.getString(R.string.easter_egg_already_activated) }
        }
    }

    private companion object {
        const val CLICKS_TO_ACTIVATE_EASTER_EGG = 5
        const val EN_US_LANGUAGE = "en-US"
        const val PT_BR_LANGUAGE = "pt-BR"
    }
}