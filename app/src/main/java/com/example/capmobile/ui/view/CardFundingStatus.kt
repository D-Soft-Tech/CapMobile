package com.example.capmobile.ui.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.CardFundingStatusBinding
import com.example.capmobile.util.AppConstants.showBarChart
import com.github.mikephil.charting.charts.BarChart

class CardFundingStatus : Fragment() {
    private lateinit var binding: CardFundingStatusBinding
    private lateinit var backArrow: ImageView
    private lateinit var backText: TextView
    private lateinit var barChart: BarChart
    private lateinit var accountStatusAdapter: ArrayAdapter<String>
    private lateinit var accoutStatusDropDown: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.card_funding_status, container, false)

        accountStatusAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.statuses_with_all)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        accoutStatusDropDown.setAdapter(accountStatusAdapter)
        // populate data into bar chart
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            barChart.showBarChart()
        }
    }

    override fun onResume() {
        super.onResume()
        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        backText.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initializeViews() {
        backArrow = binding.imageButton
        barChart = binding.barChart
        accoutStatusDropDown = binding.accountTypeTIET
        backText = binding.backText
    }
}