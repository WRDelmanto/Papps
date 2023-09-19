package com.wrdelmanto.papps.games.coinFlipper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentCoinFlipperBinding

class CoinFlipperFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentCoinFlipperBinding

    private val coinFlipperViewModel: CoinFlipperViewModel by viewModels()

    private lateinit var heads: ImageView
    private lateinit var tails: ImageView
    private lateinit var resetButton: Button

    private lateinit var resultMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinFlipperBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.coinFlipperViewModel = coinFlipperViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        heads = binding.coinFlipperHeadsImage
        tails = binding.coinFlipperTailsImage
        resetButton = binding.coinFlipperResetButton

        resultMessage = binding.coinFlipperResult

        initiateListeners()
    }

    override fun onResume() {
        super.onResume()

        coinFlipperViewModel.resetUi(context)
    }

    override fun onPause() {
        disableListeners()

        super.onPause()
    }

    private fun initiateListeners() {
        heads.setOnClickListener {
            coinFlipperViewModel.choice(
                context, resultMessage, getString(R.string.coin_flipper_heads)
            )
        }
        tails.setOnClickListener {
            coinFlipperViewModel.choice(
                context, resultMessage, getString(R.string.coin_flipper_tails)
            )
        }
        resetButton.setOnClickListener { coinFlipperViewModel.resetScore(context, resultMessage) }
    }

    private fun disableListeners() {
        heads.setOnClickListener(null)
        tails.setOnClickListener(null)
        resetButton.setOnClickListener(null)
    }
}