package com.wrdelmanto.papps.games.coinFlipper

import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.graphics.Color.GRAY
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.logD
import java.util.Random

class CoinFlipperFragment : Fragment() {
    private lateinit var resultImage: ImageView
    private lateinit var result: TextView
    private lateinit var appScore: TextView
    private lateinit var selfScore: TextView
    private lateinit var heads: ImageView
    private lateinit var tails: ImageView
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_coin_flipper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultImage = view.findViewById(R.id.coin_flipper_result_image)
        result = view.findViewById(R.id.coin_flipper_result)
        appScore = view.findViewById(R.id.coin_flipper_app_score)
        selfScore = view.findViewById(R.id.coin_flipper_self_score)
        heads = view.findViewById(R.id.coin_flipper_heads_image)
        tails = view.findViewById(R.id.coin_flipper_tails_image)
        resetButton = view.findViewById(R.id.coin_flipper_reset_button)

        initiateListeners()
    }

    private fun initiateListeners() {
        heads.setOnClickListener { choice(getString(R.string.coin_flipper_heads)) }
        tails.setOnClickListener { choice(getString(R.string.coin_flipper_tails)) }
        resetButton.setOnClickListener { resetScore() }
    }

    private fun choice(selfChoice: String) {
        val options = arrayOf(getString(R.string.coin_flipper_heads), getString(R.string.coin_flipper_tails))
        val numero = Random().nextInt(2)
        val resultCoinFlip = options[numero]

        when (resultCoinFlip) {
            getString(R.string.coin_flipper_heads) -> resultImage.setImageResource(R.drawable.coin_flipper_heads)
            getString(R.string.coin_flipper_tails) -> resultImage.setImageResource(R.drawable.coin_flipper_tails)
        }

        logD { "selfChoice=$selfChoice, resultCoinFlip=$resultCoinFlip" }

        if (selfChoice == getString(R.string.coin_flipper_heads) && resultCoinFlip == getString(R.string.coin_flipper_heads)
            || selfChoice == getString(R.string.coin_flipper_tails) && resultCoinFlip == getString(R.string.coin_flipper_tails)
        ) { // User Wins
            result.apply {
                text = getString(R.string.won)
                textSize = 32F
                setTextColor(GREEN)
            }

            val result = 1 + selfScore.text.toString().toInt()
            selfScore.text = result.toString()
        } else { // App Wins
            result.apply {
                text = getString(R.string.lost)
                textSize = 32F
                setTextColor(RED)
            }

            val result = 1 + appScore.text.toString().toInt()
            appScore.text = result.toString()
        }
    }

    private fun resetScore() {
        result.apply {
            text = getString(R.string.coin_flipper_win_or_lose)
            textSize = 20F
            setTextColor(GRAY)
        }

        appScore.text = getString(R.string.zero)
        selfScore.text = getString(R.string.zero)
        resultImage.setImageResource(R.drawable.ic_empty)

        logD { "resetScore" }
    }
}