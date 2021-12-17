package com.example.capmobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.capmobile.R
import com.example.capmobile.databinding.DashboardLayoutBinding
import com.example.capmobile.model.StatusCardDetails
import com.example.capmobile.ui.adapters.StatusCardViewPagerAdapter
import com.example.capmobile.util.AppConstants.getStatus
import com.example.capmobile.util.AppConstants.showBarChart
import com.example.capmobile.util.AppConstants.showPieChart
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DashBoard : Fragment() {
    private val statusCardAdapter by lazy { StatusCardViewPagerAdapter() }
    private lateinit var statusCardDetails: List<StatusCardDetails>
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: DashboardLayoutBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_layout, container, false)
        statusCardDetails = getStatus()
        setUpViewPager()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewPager.adapter = statusCardAdapter
            statusCardAdapter.getPageWidth(0.5f)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize views
        viewsInitialization()

        TabLayoutMediator(tabLayout, viewPager) { _, _ ->
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        pieChart.showPieChart()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                toggleChart(position)
                barChart.showBarChart()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun setUpViewPager() {
        viewPager = binding.viewPager
        statusCardAdapter.setData(statusCardDetails)
    }

    // Initializes views
    private fun viewsInitialization() {
        pieChart = binding.pieChart
        tabLayout = binding.tabLayout
        barChart = binding.barChart
        spinner = binding.spinner2
    }

    private fun toggleChart(selectedItemPosition: Int) {
        if (selectedItemPosition == 0) {
            pieChart.visibility = View.VISIBLE
            barChart.visibility = View.INVISIBLE
        }
        else if (selectedItemPosition == 1) {
            pieChart.visibility = View.INVISIBLE
            barChart.visibility = View.VISIBLE
        }
    }

}
