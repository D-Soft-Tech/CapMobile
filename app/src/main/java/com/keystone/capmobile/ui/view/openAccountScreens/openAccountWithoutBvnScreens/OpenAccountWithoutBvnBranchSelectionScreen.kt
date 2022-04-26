package com.keystone.capmobile.ui.view.openAccountScreens.openAccountWithoutBvnScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentOpenAccountWithoutBvnBranchSelectionScreenBinding
import com.google.android.material.textfield.TextInputEditText

class OpenAccountWithoutBvnBranchSelectionScreen : Fragment() {
    private lateinit var binding: FragmentOpenAccountWithoutBvnBranchSelectionScreenBinding
    private lateinit var selectAProductTv: AutoCompleteTextView
    private lateinit var previousBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var accountTypeArrayAdapter: ArrayAdapter<String>
    private lateinit var branchArrayAdapter: ArrayAdapter<String>
    private lateinit var branch: AutoCompleteTextView
    private lateinit var daoCode: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_without_bvn_branch_selection_screen,
            container,
            false
        )

        accountTypeArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.products)
        )

        branchArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.dormieBranch)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        // Set adapter to the autoCompleteTextView Dropdown
        selectAProductTv.setAdapter(accountTypeArrayAdapter)
        branch.setAdapter(branchArrayAdapter)
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
        selectAProductTv = binding.accountTypeTIET
        branch = binding.branch
        daoCode = binding.daoCodeTIET
        nextBtn = binding.nextBtn
        previousBtn = binding.previousPage
    }
}