package com.keystone.capmobile.ui.view.posCollection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentExistingCustomerScreenBinding
import com.keystone.capmobile.ui.adapters.HardCoreRecyclerViewAdapter
import com.keystone.capmobile.util.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExistingCustomerScreen @Inject constructor() : Fragment() {
    private val dummyData = AppConstants.getHardCoreData()
    private lateinit var accountsAdapter: HardCoreRecyclerViewAdapter
    private lateinit var recyclerV: RecyclerView
    private lateinit var binding: FragmentExistingCustomerScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_existing_customer_screen,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        accountsAdapter = HardCoreRecyclerViewAdapter()
        accountsAdapter.setData(dummyData)
    }

    override fun onResume() {
        super.onResume()
        recyclerV.adapter = accountsAdapter
    }

    private fun initViews() {
        with(binding) {
            recyclerV = recyclerView
        }
    }
}