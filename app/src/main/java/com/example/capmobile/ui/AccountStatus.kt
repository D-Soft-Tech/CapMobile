package com.example.capmobile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.NewAccountStatusBinding
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.util.AppConstants.showPieChart
import com.github.mikephil.charting.charts.PieChart

class AccountStatus : Fragment() {
    private lateinit var binding: NewAccountStatusBinding
    private lateinit var accountType: AutoCompleteTextView
    private lateinit var pieChart: PieChart
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var backArrow: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.new_account_status, container, false)
        arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.exposed_dropdown_menu_layout,
            resources.getStringArray(R.array.statuses)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        accountType.setAdapter(arrayAdapter)
    }

    override fun onResume() {
        super.onResume()
        pieChart.showPieChart()
        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        // Lock drawer
        (activity as DrawerLocker).setDrawerEnabled(false)
    }

    private fun initializeViews() {
        accountType = binding.autoCompleteTextView
        pieChart = binding.pieChart
        backArrow = binding.imageButton
    }
}