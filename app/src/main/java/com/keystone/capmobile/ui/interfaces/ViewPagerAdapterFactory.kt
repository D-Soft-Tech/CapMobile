package com.keystone.capmobile.ui.interfaces

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.keystone.capmobile.ui.adapters.OpenAccountViewpagerAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ViewPagerAdapterFactory {
    fun createViewPagerAdapter(
        fm: FragmentManager,
        lifecycle: Lifecycle
    ): OpenAccountViewpagerAdapter
}