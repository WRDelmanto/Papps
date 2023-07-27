package com.wrdelmanto.papps.games.unscramble

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentUnscrambleBinding

class UnscrambleFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentUnscrambleBinding

    private val unscrambleViewModel: UnscrambleViewModel by viewModels()

    private lateinit var wordInput: EditText
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnscrambleBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity?)?.updateAppBarTitle(getString(R.string.app_name_unscramble))

        binding.unscrambleViewModel = unscrambleViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        wordInput = binding.unscrambleInputWord
        resetButton = binding.unscrambleResetButton

        initiateListeners()
    }

    override fun onResume() {
        super.onResume()

        unscrambleViewModel.resetUi(context)
    }

    override fun onDestroyView() {
        disableListeners()

        super.onDestroyView()
    }

    private fun initiateListeners() {
        wordInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                unscrambleViewModel.checkAnswer(context, s.toString())
                if (unscrambleViewModel.isAnswerCorrect.value == true) clearInputText()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        resetButton.setOnClickListener {
            clearInputText()
            unscrambleViewModel.reset(context)
        }
    }

    private fun disableListeners() {
        wordInput.addTextChangedListener(null)
        resetButton.setOnClickListener(null)
    }

    fun clearInputText() {
        wordInput.setText("", null)
    }
}