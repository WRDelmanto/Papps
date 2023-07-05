package com.wrdelmanto.papps.apps.dice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentDiceBinding
import com.wrdelmanto.papps.utils.SP_D_DICE_HISTORY
import com.wrdelmanto.papps.utils.getSharedPreferences
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class DiceFragment : Fragment() {
    private lateinit var binding: FragmentDiceBinding

    private lateinit var clickAnywhere: TextView
    private lateinit var result: ImageView
    private lateinit var clickHere: Button

    private lateinit var diceHistory: String

    private lateinit var firstHistory: TextView
    private lateinit var secondHistory: TextView
    private lateinit var thirdHistory: TextView
    private lateinit var fourthHistory: TextView
    private lateinit var fifthHistory: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiceBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickAnywhere = binding.diceClickAnywhere
        result = binding.diceResult
        clickHere = binding.diceClickHere

        firstHistory = binding.diceHistoryFirst
        secondHistory = binding.diceHistorySecond
        thirdHistory = binding.diceHistoryThird
        fourthHistory = binding.diceHistoryFourth
        fifthHistory = binding.diceHistoryFifth
    }

    override fun onResume() {
        super.onResume()

        resetUi()
        initiateListeners()
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() = clickHere.setOnClickListener {
        stopBlinkingAnimation(clickAnywhere)
        rollDice()
    }

    private fun disableListeners() = clickHere.setOnClickListener(null)

    private fun resetUi() {
        (activity as MainActivity?)?.updateAppBarTitle(getString(R.string.app_name_dice))

        clickAnywhere.visibility = VISIBLE
        result.visibility = INVISIBLE

        diceHistory = context?.let {
            val lh = getSharedPreferences(it, SP_D_DICE_HISTORY, String)
            lh ?: "*****"
        } as String

        updateDiceHistory()

        startBlinkingAnimation(clickAnywhere)
    }

    private fun rollDice() {
        val diceResult = (1..6).random()

        clickAnywhere.visibility = INVISIBLE
        result.visibility = VISIBLE

        if (diceHistory.length >= 5) diceHistory = diceHistory.dropLast(1)
        diceHistory = diceResult.toString() + diceHistory

        context?.let { putSharedPreferences(it, SP_D_DICE_HISTORY, diceHistory) }

        result.background = when (diceResult) {
            1 -> ResourcesCompat.getDrawable(resources, R.drawable.dice_1, null)
            2 -> ResourcesCompat.getDrawable(resources, R.drawable.dice_2, null)
            3 -> ResourcesCompat.getDrawable(resources, R.drawable.dice_3, null)
            4 -> ResourcesCompat.getDrawable(resources, R.drawable.dice_4, null)
            5 -> ResourcesCompat.getDrawable(resources, R.drawable.dice_5, null)
            else -> ResourcesCompat.getDrawable(resources, R.drawable.dice_6, null)
        }

        updateDiceHistory()
    }

    private fun updateDiceHistory() {
        firstHistory.text = if (diceHistory[0].toString() == "*") "" else diceHistory[0].toString()
        secondHistory.text = if (diceHistory[1].toString() == "*") "" else diceHistory[1].toString()
        thirdHistory.text = if (diceHistory[2].toString() == "*") "" else diceHistory[2].toString()
        fourthHistory.text = if (diceHistory[3].toString() == "*") "" else diceHistory[3].toString()
        fifthHistory.text = if (diceHistory[4].toString() == "*") "" else diceHistory[4].toString()
    }
}