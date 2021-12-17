package com.example.capmobile.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OpenAccountViewpagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val screens: ArrayList<Fragment> = arrayListOf()

    override fun getItemCount() = screens.size

    override fun createFragment(position: Int) = screens[position]

    fun setScreens(pathFlows: ArrayList<Fragment>) {
        screens.addAll(pathFlows)
    }
}