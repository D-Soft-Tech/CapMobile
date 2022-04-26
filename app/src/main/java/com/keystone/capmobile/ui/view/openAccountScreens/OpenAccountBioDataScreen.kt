package com.keystone.capmobile.ui.view.openAccountScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.airbnb.paris.extensions.style
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentOpenAccountBioDataScreenBinding
import com.keystone.capmobile.ui.viewModel.OpenAccountViewModel
import com.keystone.capmobile.util.ConstantsOnly.notifyErrorValidation
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OpenAccountBioDataScreen @Inject constructor() : Fragment() {
    private lateinit var binding: FragmentOpenAccountBioDataScreenBinding
    private val viewModel: OpenAccountViewModel by activityViewModels()
    private lateinit var previousBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var myAdapter: ArrayAdapter<String>
    private lateinit var gender: AutoCompleteTextView
    private lateinit var genderEnclosingParentTextInputLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open_account_bio_data_screen,
            container,
            false
        )
        myAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.gender)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        gender.setAdapter(myAdapter)
        binding.apply {
            lifecycleOwner = this@OpenAccountBioDataScreen.viewLifecycleOwner
            viewModel.bvnResponse.observe(viewLifecycleOwner, {
                bvnResponse = it.data
            })
        }
    }

    override fun onResume() {
        super.onResume()
        previousBtn.setOnClickListener {
            (requireParentFragment() as NewAccountOpeningViewPager).gotoPreviousStep()
        }
        nextBtn.setOnClickListener {
            if (gender.text.toString() == "Male" || gender.text.toString() == "Female") {
                viewModel.setUserGender(gender.text.toString())
                (requireParentFragment() as NewAccountOpeningViewPager).gotoNextStep()
            } else {
                genderEnclosingParentTextInputLayout.notifyErrorValidation()
            }
        }
        gender.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedGender = parent.getItemAtPosition(position).toString()
                if (selectedGender == "Male" || selectedGender == "Female") {
                    genderEnclosingParentTextInputLayout.style(R.style.lekanTilStyle)
                } else {
                    genderEnclosingParentTextInputLayout.notifyErrorValidation()
                }
            }
    }

    private fun initializeViews() {
        with(binding) {
            gender = accountTypeTIET
            previousBtn = previousPage
            nextBtn = nextButton
            genderEnclosingParentTextInputLayout = genderTil
        }
    }
}