package com.wrdelmanto.papps.apps.bodyMassIndex

import android.content.Context
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.databinding.FragmentBodyMassIndexBinding

class BodyMassIndexFragment(
    private val context: Context,
) : Fragment() {
    private lateinit var binding: FragmentBodyMassIndexBinding

    private val bodyMassIndexViewModel: BodyMassIndexViewModel by viewModels()

    private lateinit var heightInput: EditText
    private lateinit var weightInput: EditText

    private lateinit var bmiOutput: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBodyMassIndexBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(M)
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.bodyMassIndexViewModel = bodyMassIndexViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        heightInput = binding.bodyMassIndexHeightInput
        weightInput = binding.bodyMassIndexWeightInput

        bmiOutput = binding.bodyMassIndexResult

        initiateListeners()
        initiateObservers()
    }

    override fun onResume() {
        super.onResume()

        bodyMassIndexViewModel.resetUi(context)
    }

    override fun onDestroy() {
        disableListeners()

        super.onDestroy()
    }

    private fun initiateListeners() {
        heightInput.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    bodyMassIndexViewModel.updateHeight(s.toString())
                    bodyMassIndexViewModel.calculateBMI(context)
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                    // Do nothing
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    // Do nothing
                }
            },
        )

        weightInput.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    bodyMassIndexViewModel.updateWeight(s.toString())
                    bodyMassIndexViewModel.calculateBMI(context)
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                    // Do nothing
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int,
                ) {
                    // Do nothing
                }
            },
        )
    }

    @RequiresApi(M)
    private fun initiateObservers() {
        bodyMassIndexViewModel.bmi.observe(viewLifecycleOwner) {
            when {
                it == "?" -> bmiOutput.setTextColor(resources.getColor(R.color.black, null))

                it.toDouble() < 18.50 ->
                    bmiOutput.setTextColor(
                        resources.getColor(
                            R.color.light_blue,
                            null,
                        ),
                    )

                it.toDouble() in 18.50..24.90 ->
                    bmiOutput.setTextColor(
                        resources.getColor(
                            R.color.green,
                            null,
                        ),
                    )

                it.toDouble() in 24.90..30.00 ->
                    bmiOutput.setTextColor(
                        resources.getColor(
                            R.color.orange,
                            null,
                        ),
                    )

                it.toDouble() > 30.00 ->
                    bmiOutput.setTextColor(
                        resources.getColor(
                            R.color.red,
                            null,
                        ),
                    )

                else -> bmiOutput.setTextColor(resources.getColor(R.color.black, null))
            }
        }
    }

    private fun disableListeners() {
        heightInput.removeTextChangedListener(null)
        weightInput.removeTextChangedListener(null)
    }
}
