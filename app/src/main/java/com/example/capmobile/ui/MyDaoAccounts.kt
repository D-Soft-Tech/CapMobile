package com.example.capmobile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentMyDaoAccountsBinding
import com.example.capmobile.ui.adapters.DaoAccountAdapters
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.util.AppConstants.dormantDataForChannelsObjects

class MyDaoAccounts : Fragment() {
    private val adapter: DaoAccountAdapters by lazy { DaoAccountAdapters() }
    private lateinit var binding: FragmentMyDaoAccountsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var backArrow: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_dao_accounts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        // Feed data into adapter
        adapter.setData(dormantDataForChannelsObjects())
        // Set adapter to recyclerView
        recyclerView.adapter = adapter

        // go back at the onclick of the back arrow
        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()
        // Disable drawer from showing on this page
        (activity as DrawerLocker).setDrawerEnabled(false)
    }

    private fun initializeViews() {
        recyclerView = binding.recyclerView
        backArrow = binding.imageButton
    }
}