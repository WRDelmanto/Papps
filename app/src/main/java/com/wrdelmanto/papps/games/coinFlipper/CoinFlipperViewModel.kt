package com.wrdelmanto.papps.games.coinFlipper

import android.content.Context
import android.graphics.Color.GRAY
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.logD
import java.util.Random

class CoinFlipperViewModel : ViewModel() {
    private val _resultImage = MutableLiveData<Drawable>()
    val resultImage: MutableLiveData<Drawable>
        get() = _resultImage

    private val _selfScore = MutableLiveData("0")
    val selfScore: MutableLiveData<String>
        get() = _selfScore

    private val _appScore = MutableLiveData("0")
    val appScore: MutableLiveData<String>
        get() = _appScore

    private val _resultMessage = MutableLiveData("Ganhou ou Perdeu?")
    val resultMessage: MutableLiveData<String>
        get() = _resultMessage

    fun resetUi(context: Context) {
        _resultImage.value =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)
    }

    fun choice(context: Context, resultMessage: TextView, selfChoice: String) {
        val options = arrayOf(
            context.resources.getString(R.string.coin_flipper_heads),
            context.resources.getString(R.string.coin_flipper_tails)
        )
        val numero = Random().nextInt(2)
        val resultCoinFlip = options[numero]

        _resultImage.value = when (resultCoinFlip) {
            context.resources.getString(R.string.coin_flipper_heads) -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.coin_flipper_heads, null
            )

            context.resources.getString(R.string.coin_flipper_tails) -> ResourcesCompat.getDrawable(
                context.resources, R.drawable.coin_flipper_tails, null
            )

            else -> {
                ResourcesCompat.getDrawable(context.resources, R.drawable.coin_flipper_heads, null)
            }
        }

        logD { "selfChoice=$selfChoice, resultCoinFlip=$resultCoinFlip" }

        if (selfChoice == context.resources.getString(R.string.coin_flipper_heads) && resultCoinFlip == context.resources.getString(
                R.string.coin_flipper_heads
            ) || selfChoice == context.resources.getString(
                R.string.coin_flipper_tails
            ) && resultCoinFlip == context.resources.getString(R.string.coin_flipper_tails)
        ) { // User Wins
            _selfScore.value = (_selfScore.value?.toInt()?.plus(1)).toString()

            resultMessage.apply {
                text = context.resources.getString(R.string.won)
                textSize = 32F
                setTextColor(GREEN)
            }
        } else { // App Wins
            _appScore.value = (_appScore.value?.toInt()?.plus(1)).toString()

            resultMessage.apply {
                text = context.resources.getString(R.string.lost)
                textSize = 32F
                setTextColor(RED)
            }
        }
    }

    fun resetScore(context: Context, resultMessage: TextView) {
        resultMessage.apply {
            text = context.resources.getString(R.string.coin_flipper_win_or_lose)
            textSize = 20F
            setTextColor(GRAY)
        }

        _appScore.value = context.resources.getString(R.string.zero)
        _selfScore.value = context.resources.getString(R.string.zero)
        _resultImage.value =
            ResourcesCompat.getDrawable(context.resources, R.drawable.ic_empty, null)

        logD { "resetScore" }
    }
}