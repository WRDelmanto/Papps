package com.wrdelmanto.papps.apps.clickCounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.utils.logD

class ClickCounterFragment : Fragment() {
    private lateinit var counter: TextView
    private lateinit var additionButton: Button
    private lateinit var clickAnywhere: TextView
    private lateinit var resetButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_click_counter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        counter = view.findViewById(R.id.click_counter_counter)
        additionButton = view.findViewById(R.id.click_counter_addition_button)
        clickAnywhere = view.findViewById(R.id.click_counter_click_anywhere)
        resetButton = view.findViewById(R.id.click_counter_reset_button)

        initiateListeners()
    }

    private fun initiateListeners() {
        additionButton.setOnClickListener { addition() }
        resetButton.setOnClickListener { resetCounter() }
    }

    private fun addition() {
        clickAnywhere.isVisible = false

        val clicks = 1 + counter.text.toString().toInt()
        counter.text = clicks.toString()

        logD { "clicks=$clicks" }
    }

    private fun resetCounter() {
        clickAnywhere.isVisible = true
        counter.text = getString(R.string.zero)

        logD { "resetCounter" } }
}