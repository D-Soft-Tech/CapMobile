package com.example.capmobile.ui.reactivateAccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.capmobile.R
import com.example.capmobile.databinding.AddressScreenBinding
import com.example.capmobile.databinding.FragmentReactivateAccountUserAddressBinding
import com.example.capmobile.ui.openAccountScreens.FragmentOpenAccount

class ReactivateAccountUserAddress : Fragment() {

    private lateinit var binding: FragmentReactivateAccountUserAddressBinding
    private var progressTracker: Int = 0
    private var currentItemPositionInGenderTV: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reactivate_account_user_address, container, false)
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
        (requireParentFragment() as ReactivateAccount).setSpecifications(
            2,
            resources.getString(R.string.userAddressScreenTitle),
            0
        )

        binding.preferredBranch.setOnItemClickListener { parent, view, position, id ->
            if(position >= 0) { // Condition that must be met
                (requireParentFragment() as ReactivateAccount).enableAButton(
                    1,
                    progressTracker
                )
                ++progressTracker
            }
            if (currentItemPositionInGenderTV > 0) {
                (requireParentFragment() as ReactivateAccount).enableAButton(1, progressTracker)
                ++progressTracker
            }
        }
    }

    override fun onPause() {
        super.onPause()
        (requireParentFragment() as ReactivateAccount).undoSpecifications()
    }
}