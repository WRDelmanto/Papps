package com.wrdelmanto.papps.games.tipTacToe

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.databinding.FragmentTicTacToeBinding

class TicTacToeFragment(
    private val context: Context
) : Fragment() {
    private lateinit var binding: FragmentTicTacToeBinding

    private val ticTacToeViewModel: TicTacToeViewModel by viewModels()

    private lateinit var a11Button: TextView
    private lateinit var a12Button: TextView
    private lateinit var a13Button: TextView
    private lateinit var a21Button: TextView
    private lateinit var a22Button: TextView
    private lateinit var a23Button: TextView
    private lateinit var a31Button: TextView
    private lateinit var a32Button: TextView
    private lateinit var a33Button: TextView

    private lateinit var modeButton: Button
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicTacToeBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ticTacToeViewModel = ticTacToeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        a11Button = binding.ticTacToeA11
        a12Button = binding.ticTacToeA12
        a13Button = binding.ticTacToeA13
        a21Button = binding.ticTacToeA21
        a22Button = binding.ticTacToeA22
        a23Button = binding.ticTacToeA23
        a31Button = binding.ticTacToeA31
        a32Button = binding.ticTacToeA32
        a33Button = binding.ticTacToeA33

        modeButton = binding.ticTacToeModeButton
        resetButton = binding.ticTacToeResetButton

        initiateListeners()
        initiateObservers()
    }

    override fun onResume() {
        super.onResume()

        ticTacToeViewModel.resetUi(context, isFirstTime = true)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initiateListeners() {
        a11Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a11Button
            )
        }
        a12Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a12Button
            )
        }
        a13Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a13Button
            )
        }
        a21Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a21Button
            )
        }
        a22Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a22Button
            )
        }
        a23Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a23Button
            )
        }
        a31Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a31Button
            )
        }
        a32Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a32Button
            )
        }
        a33Button.setOnClickListener {
            ticTacToeViewModel.buttonSelected(
                context, requireView(), a33Button
            )
        }

        modeButton.setOnClickListener { ticTacToeViewModel.updateGameMode(context) }
        resetButton.setOnClickListener {
            ticTacToeViewModel.resetUi(
                context, shouldResetModeButton = false
            )
        }
    }

    private fun initiateObservers() {
        ticTacToeViewModel.winner.observe(viewLifecycleOwner) { winner ->
            if (winner != -1) disableAllButtons()
            else enableAllButtons()
        }
    }

    private fun disableListeners() {
        a11Button.setOnClickListener(null)
        a12Button.setOnClickListener(null)
        a13Button.setOnClickListener(null)
        a21Button.setOnClickListener(null)
        a22Button.setOnClickListener(null)
        a23Button.setOnClickListener(null)
        a31Button.setOnClickListener(null)
        a32Button.setOnClickListener(null)
        a33Button.setOnClickListener(null)

        modeButton.setOnClickListener(null)
        resetButton.setOnClickListener(null)
    }

    private fun enableAllButtons() {
        a11Button.isEnabled = true
        a12Button.isEnabled = true
        a13Button.isEnabled = true
        a21Button.isEnabled = true
        a22Button.isEnabled = true
        a23Button.isEnabled = true
        a31Button.isEnabled = true
        a32Button.isEnabled = true
        a33Button.isEnabled = true
    }

    private fun disableAllButtons() {
        a11Button.isEnabled = false
        a12Button.isEnabled = false
        a13Button.isEnabled = false
        a21Button.isEnabled = false
        a22Button.isEnabled = false
        a23Button.isEnabled = false
        a31Button.isEnabled = false
        a32Button.isEnabled = false
        a33Button.isEnabled = false
    }
}