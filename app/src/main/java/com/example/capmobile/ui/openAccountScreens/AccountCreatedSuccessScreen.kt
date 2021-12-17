package com.example.capmobile.ui.openAccountScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.capmobile.R
import com.example.capmobile.databinding.AccountCreatedBinding

class AccountCreatedSuccessScreen : Fragment() {
    private lateinit var binding: AccountCreatedBinding
    private var progressTracker: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.account_created, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (requireParentFragment() as FragmentOpenAccount).downLoadAccountInfo()
    }
}