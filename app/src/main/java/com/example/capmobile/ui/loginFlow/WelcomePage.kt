package com.example.capmobile.ui.loginFlow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentWelcomePageBinding
import com.example.capmobile.ui.MainActivity
import com.example.capmobile.util.AppConstants.showBarChart
import com.github.mikephil.charting.charts.BarChart

class WelcomePage : Fragment() {
    private lateinit var binding: FragmentWelcomePageBinding
    private lateinit var barChart: BarChart
    private lateinit var proceedBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        barChart.showBarChart()
        proceedBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun initializeViews() {
        barChart = binding.barChart
        proceedBtn = binding.proceed
    }
}