package com.wrdelmanto.papps

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.utils.SP_EASTER_EGG
import com.wrdelmanto.papps.utils.getSharedPreferences

class SharedViewModel : ViewModel() {
    val easterEggActivated = MutableLiveData(false)

    private lateinit var currentFragmentTag: String

    fun shouldShowRandomBottomNavMenu(): Boolean =
        currentFragmentTag in listOf(RANDOM_LETTER, RANDOM_NUMBER)

    fun checkEasterEgg(context: Context) {
        easterEggActivated.value =
            ((getSharedPreferences(context, SP_EASTER_EGG, Boolean)) ?: false).toString()
                .toBoolean()
    }

    fun getCurrentTitle(context: Context, tag: String): String {
        currentFragmentTag = tag

        return when (currentFragmentTag) {
            HOME -> HOME
            CLICK_COUNTER -> context.resources.getString(R.string.app_name_click_counter)
            MONEY_CONVERTER -> context.resources.getString(R.string.app_name_money_converter)
            BODY_MASS_INDEX -> context.resources.getString(R.string.app_name_body_mass_index)
            DICES -> context.resources.getString(R.string.app_name_dices)
            RANDOM_LETTER -> context.resources.getString(R.string.app_name_random_letter)
            NASA_PICTURE_OF_THE_DAY -> context.resources.getString(R.string.app_name_nasa_picture_of_the_day)
            RANDOM_NUMBER -> context.resources.getString(R.string.app_name_random_number)
            TIP -> context.resources.getString(R.string.app_name_tip)
            COIN_FLIPPER -> context.resources.getString(R.string.app_name_coin_flipper)
            TIC_TAC_TOE -> context.resources.getString(R.string.app_name_tic_tac_toe)
            ROCK_PAPER_SCISSORS -> context.resources.getString(R.string.app_name_rock_paper_scissors)
            UNSCRAMBLE -> context.resources.getString(R.string.app_name_unscramble)
            SPEED_TEST -> context.resources.getString(R.string.app_name_speed_test)
            else -> EMPTY_STRING
        }
    }

    companion object {
        const val HOME = ""
        const val CLICK_COUNTER = "CLICK_COUNTER"
        const val MONEY_CONVERTER = "MONEY_CONVERTER"
        const val DICES = "DICES"
        const val BODY_MASS_INDEX = "BODY_MASS_INDEX"
        const val RANDOM_LETTER = "RANDOM_LETTER"
        const val NASA_PICTURE_OF_THE_DAY = "NASA_PICTURE_OF_THE_DAY"
        const val RANDOM_NUMBER = "RANDOM_NUMBER"
        const val TIP = "TIP"
        const val COIN_FLIPPER = "COIN_FLIPPER"
        const val TIC_TAC_TOE = "TIC_TAC_TOE"
        const val ROCK_PAPER_SCISSORS = "ROCK_PAPER_SCISSORS"
        const val UNSCRAMBLE = "UNSCRAMBLE"
        const val SPEED_TEST = "SPEED_TEST"
        const val EMPTY_STRING = ""
    }
}