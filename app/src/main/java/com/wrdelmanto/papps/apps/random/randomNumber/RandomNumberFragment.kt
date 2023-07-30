package com.wrdelmanto.papps.apps.random.randomNumber

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentRandomNumberBinding
import com.wrdelmanto.papps.utils.SP_RN_MAX
import com.wrdelmanto.papps.utils.SP_RN_MIN
import com.wrdelmanto.papps.utils.hideKeyboard
import com.wrdelmanto.papps.utils.putSharedPreferences
import com.wrdelmanto.papps.utils.startBlinkingAnimation
import com.wrdelmanto.papps.utils.stopBlinkingAnimation

class RandomNumberFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentRandomNumberBinding

    private val randomNumberViewModel: RandomNumberViewModel by viewModels()

    private lateinit var result: TextView
    private lateinit var randomizerButton: Button
    private lateinit var minInput: EditText
    private lateinit var maxInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomNumberBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.randomNumberViewModel = randomNumberViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        result = binding.randomNumberResult
        randomizerButton = binding.randomNumberClickAnywhereButton
        minInput = binding.randomNumberMinInput
        maxInput = binding.randomNumberMaxInput

        initiateListeners()
    }

    @Suppress("Deprecation")
    override fun onResume() {
        super.onResume()

        randomNumberViewModel.resetUi(context)

        result.apply {
            textSize = 16F
            setTextColor(resources.getColor(R.color.defaul_text_color))
        }

        startBlinkingAnimation(result)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    @Suppress("Deprecation")
    private fun initiateListeners() {
        randomizerButton.setOnClickListener {
            hideKeyboard()

            stopBlinkingAnimation(result, true)

            result.apply {
                textSize = 128F
                setTextColor(resources.getColor(R.color.color_secondary))
            }

            randomNumberViewModel.generateRandomNumber(context)
        }

        minInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                putSharedPreferences(context, SP_RN_MIN, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        maxInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                putSharedPreferences(context, SP_RN_MAX, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
    }

    private fun disableListeners() {
        randomizerButton.setOnClickListener(null)
        minInput.setOnClickListener(null)
        maxInput.setOnClickListener(null)
    }
}