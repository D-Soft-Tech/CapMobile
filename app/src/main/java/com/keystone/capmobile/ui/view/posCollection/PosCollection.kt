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
import androidx.viewpager2.widget.ViewPager2
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentPosCollectionBinding
import com.keystone.capmobile.ui.adapters.OpenAccountViewpagerAdapter
import com.keystone.capmobile.ui.interfaces.DrawerLocker
import com.keystone.capmobile.ui.interfaces.ViewPagerAdapterFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PosCollection @Inject constructor(): Fragment() {
    private lateinit var viewPagerDestinationTexts: Array<String>
    private lateinit var binding: FragmentPosCollectionBinding
    private lateinit var viewPagerAdapter: OpenAccountViewpagerAdapter
    @Inject
    lateinit var viewPagerAdapterFactory: ViewPagerAdapterFactory
    @Inject
    lateinit var existingCustomer: ExistingCustomerScreen
    @Inject
    lateinit var targetCustomer: TargetCustomer
    private var screensToPassToViewPager: ArrayList<Fragment> = arrayListOf()
    private lateinit var backArrowImageView: ImageView
    private lateinit var backTextView: TextView
    private lateinit var viewPagerView: ViewPager2
    private lateinit var tabLayoutView: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pos_collection, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        screensToPassToViewPager = arrayListOf(existingCustomer, targetCustomer)
        viewPagerDestinationTexts = resources.getStringArray(R.array.posViewPagerArray)
        // Field variables Initialization
        tabLayoutView = binding.tabLayout
        viewPagerAdapter = viewPagerAdapterFactory.createViewPagerAdapter(parentFragmentManager, lifecycle)
        viewPagerAdapter.setScreens(screensToPassToViewPager)
        viewPagerView = binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = viewPagerAdapter
        }

        // Setup the viewpager and the tab layout with Tab layout mediator
        TabLayoutMediator(tabLayoutView, viewPagerView) { tab, position ->
            tab.text = viewPagerDestinationTexts[position]
        }.attach()
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        backArrowImageView.setOnClickListener {
            findNavController().popBackStack()
        }
        // Disable the back from showing
        (activity as DrawerLocker).setDrawerEnabled(false)

        backTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        backArrowImageView.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initializeViews() {
        with(binding) {
            backArrowImageView = backArrow
            backTextView = backText
            viewPagerView = viewPager
            tabLayoutView = tabLayout
        }
    }
}