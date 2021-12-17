package com.example.capmobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.capmobile.R
import com.example.capmobile.databinding.CardFundingStatusBinding

class CardFunding: Fragment() {
    private lateinit var binding: CardFundingStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.card_funding_status, container, false)
        return binding.root
    }
}