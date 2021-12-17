package com.example.capmobile.ui.reactivateAccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import com.example.capmobile.R
import com.example.capmobile.databinding.OpenAccountFirstScreenBinding
import com.example.capmobile.databinding.ReactivateAccountFirstPageBinding
import com.example.capmobile.ui.openAccountScreens.FragmentOpenAccount

class AccountReactivationFirstPage : Fragment() {

    private lateinit var binding: ReactivateAccountFirstPageBinding
    private lateinit var selectAProductTv: AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var currentItemPositionInGenderTV: Int = 0
    private var progressTrackKeeper: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.products)
        )

        binding =
            DataBindingUtil.inflate(inflater, R.layout.reactivate_account_first_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        selectAProductTv.setAdapter(arrayAdapter)
        enableNextButton()
    }

    // Enables next button if the getBvn button is enabled and a product has been selected
    private fun enableNextButton() {
        (requireParentFragment() as ReactivateAccount).setSpecifications(
            1,
            resources.getString(R.string.getUserDetails),
            2
        )
        binding.auto.setOnItemClickListener { _, _, position: Int, _ ->
            currentItemPositionInGenderTV = position
            if (position > 0) { // Condition that must be met, update this later
                (requireParentFragment() as ReactivateAccount).enableAButton(1, progressTrackKeeper)
                progressTrackKeeper += 1
            }
        }

        if (currentItemPositionInGenderTV > 0) {
            (requireParentFragment() as ReactivateAccount).enableAButton(1, progressTrackKeeper)
            progressTrackKeeper += 1
        }

    }

    private fun initializeViews() {
        // Current Views
        selectAProductTv = binding.auto
    }

    override fun onPause() {
        super.onPause()
        (requireParentFragment() as ReactivateAccount).undoSpecifications()
    }
}