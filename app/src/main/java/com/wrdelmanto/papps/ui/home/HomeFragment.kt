package com.wrdelmanto.papps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.wrdelmanto.papps.MainActivity
import com.wrdelmanto.papps.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeLogo: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeLogo = binding.homeLogo
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

    private fun initiateListeners() = homeLogo.setOnClickListener { test() }

    private fun disableListeners() = homeLogo.setOnClickListener(null)

    private fun resetUi() = (activity as MainActivity?)?.updateAppBarTitle("")

    /**
     * Function created to test features.
     */
    @Suppress("EmptyMethod")
    private fun test() {
        // This is a test function
    }
}