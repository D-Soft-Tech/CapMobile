package com.example.capmobile.ui.view.openAccountScreens.openAccountWithoutBvnScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentOpenAccountWitoutBvnAddressScreenBinding

class OpenAccountWitoutBvnAddressScreen : Fragment() {
    private lateinit var binding: FragmentOpenAccountWitoutBvnAddressScreenBinding
    private lateinit var previousBtn: Button
    private lateinit var nextBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_witout_bvn_address_screen,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        previousBtn.setOnClickListener {
            (requireParentFragment() as OpenAccountWithoutBvnViewPager).gotoPreviousStep()
        }

        nextBtn.setOnClickListener {
            (requireParentFragment() as OpenAccountWithoutBvnViewPager).gotoNextStep()
        }
    }

    private fun initializeViews() {
        previousBtn = binding.previousPage
        nextBtn = binding.proceedBtn
    }
}