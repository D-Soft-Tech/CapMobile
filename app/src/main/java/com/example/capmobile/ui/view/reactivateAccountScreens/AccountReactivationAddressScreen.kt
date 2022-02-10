package com.example.capmobile.ui.view.reactivateAccountScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentAccountReactivationAddressScreenBinding

class AccountReactivationAddressScreen : Fragment() {
    private lateinit var binding: FragmentAccountReactivationAddressScreenBinding
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var selectBranch: AutoCompleteTextView
    private lateinit var previousBtn: Button
    private lateinit var nextBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_reactivation_address_screen,
            container,
            false
        )

        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.dormieBranch)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        selectBranch.setAdapter(arrayAdapter)
    }

    override fun onResume() {
        super.onResume()
        previousBtn.setOnClickListener {
            (requireParentFragment() as ReactivateAccountViewPager).gotoPreviousStep()
        }

        nextBtn.setOnClickListener {
            (requireParentFragment() as ReactivateAccountViewPager).gotoNextStep()
        }
    }

    private fun initializeViews() {
        selectBranch = binding.branch
        previousBtn = binding.previousPage
        nextBtn = binding.proceedBtn
    }
}