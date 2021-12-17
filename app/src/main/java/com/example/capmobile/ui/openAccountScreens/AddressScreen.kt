package com.example.capmobile.ui.openAccountScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.capmobile.R
import com.example.capmobile.databinding.AddressScreenBinding

class AddressScreen : Fragment() {
    private lateinit var binding: AddressScreenBinding
    private var progressTracker: Int = 0
    private var currentItemPositionInGenderTV: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.address_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.auto.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.exposed_dropdown_menu_layout,
                resources.getStringArray(R.array.nigeria_states)
            )
        )

        binding.preferredBranch.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.exposed_dropdown_menu_layout,
                resources.getStringArray(R.array.dormieBranch)
            )
        )
    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment() as FragmentOpenAccount).setSpecifications(
            2,
            resources.getString(R.string.userAddressScreenTitle),
            0
        )

        binding.preferredBranch.setOnItemClickListener { parent, view, position, id ->
            if(position >= 0) { // Condition that must be met
                (requireParentFragment() as FragmentOpenAccount).enableAButton(
                    1,
                    progressTracker
                )
                ++progressTracker
            }
            if (currentItemPositionInGenderTV > 0) {
                (requireParentFragment() as FragmentOpenAccount).enableAButton(1, progressTracker)
                ++progressTracker
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (requireParentFragment() as FragmentOpenAccount).undoSpecifications()
    }
}