package com.keystone.capmobile.ui.interfaces

import com.keystone.capmobile.ui.adapters.Callback
import com.keystone.capmobile.ui.adapters.DaoAccountAdapters
import dagger.assisted.AssistedFactory

@AssistedFactory
interface DaoAccountAdapterFactory {
    fun createDaoAccountAdapters(
        callback: Callback
    ): DaoAccountAdapters
}