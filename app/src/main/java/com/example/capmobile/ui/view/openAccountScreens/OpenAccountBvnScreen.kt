package com.example.capmobile.ui.view.openAccountScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentOpenAccountBvnScreenBinding
import com.google.android.material.textfield.TextInputEditText

class OpenAccountBvnScreen : Fragment() {
    private lateinit var binding: FragmentOpenAccountBvnScreenBinding
    private lateinit var selectAProductTv: AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var bvn: TextInputEditText
    private lateinit var existingAccount: TextInputEditText
    private lateinit var daoCode: TextInputEditText
    private lateinit var continueBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_bvn_screen,
            container,
            false
        )

        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.products)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        // Set adapter to the autoCompleteTextView Dropdown
        selectAProductTv.setAdapter(arrayAdapter)
        continueBtn.setOnClickListener {
            (requireParentFragment() as NewAccountOpeningViewPager).gotoNextStep()
        }
    }

    private fun initializeViews() {
        selectAProductTv = binding.accountTypeTIET
        bvn = binding.bvn
        existingAccount = binding.existingAccount
        daoCode = binding.daoCodeTIET
        continueBtn = binding.proceedBtn
    }

}