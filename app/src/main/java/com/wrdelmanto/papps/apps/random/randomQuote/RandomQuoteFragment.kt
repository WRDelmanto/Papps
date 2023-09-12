package com.wrdelmanto.papps.apps.random.randomQuote

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.databinding.FragmentRandomQuoteBinding

class RandomQuoteFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentRandomQuoteBinding

    private val randomQuoteViewModel: RandomQuoteViewModel by viewModels()

    private lateinit var quote: TextView
    private lateinit var author: TextView

    private lateinit var loading: ProgressBar
    private lateinit var resetButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomQuoteBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.randomQuoteViewModel = randomQuoteViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        quote = binding.randomQuotesCurrentQuote
        author = binding.randomQuotesCurrentAuthor

        loading = binding.randomQuotesLoading
        resetButton = binding.randomQuotesResetButton

        initiateListeners()
        initiateDownloadObservers()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()

        randomQuoteViewModel.resetUi(context)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateListeners() {
        resetButton.setOnClickListener { randomQuoteViewModel.resetUi(context) }
    }

    private fun initiateDownloadObservers() {
        randomQuoteViewModel.state.observe(viewLifecycleOwner) {
            changeDownloadState(it.pensadorQuoteState)
        }
    }

    private fun changeDownloadState(pensadorQuoteState: PensadorQuoteState) {
        when (pensadorQuoteState) {
            PensadorQuoteState.LOADING -> {
                quote.visibility = GONE
                author.visibility = GONE

                loading.visibility = VISIBLE
                resetButton.isClickable = false
            }

            PensadorQuoteState.LOADED -> {
                quote.visibility = VISIBLE
                author.visibility = VISIBLE

                loading.visibility = GONE
                resetButton.isClickable = true
            }
        }
    }

    private fun disableListeners() {
        resetButton.setOnClickListener(null)
    }
}