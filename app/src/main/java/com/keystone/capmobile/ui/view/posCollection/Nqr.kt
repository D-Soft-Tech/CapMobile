package com.keystone.capmobile.ui.view.posCollection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentNqrBinding
import com.keystone.capmobile.ui.adapters.HardCoreAdapter2
import com.keystone.capmobile.util.AppConstants

class Nqr : Fragment() {
    private lateinit var binding: FragmentNqrBinding
    private val dummyData = AppConstants.getHardCoreData()
    private lateinit var accountsAdapter: HardCoreAdapter2
    private lateinit var recyclerV: RecyclerView
    private lateinit var backArrowImageView: ImageView
    private lateinit var backTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nqr, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        accountsAdapter = HardCoreAdapter2()
        accountsAdapter.setData(dummyData)
    }

    override fun onResume() {
        super.onResume()
        recyclerV.adapter = accountsAdapter
        backArrowImageView.setOnClickListener {
            findNavController().popBackStack()
        }
        backTextView.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViews() {
        with(binding) {
            recyclerV = recyclerView
            backArrowImageView = backArrow
            backTextView = backText
        }
    }
}