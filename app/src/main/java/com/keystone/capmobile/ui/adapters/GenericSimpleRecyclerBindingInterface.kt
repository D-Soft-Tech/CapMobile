package com.keystone.capmobile.ui.adapters

import android.view.View

interface GenericSimpleRecyclerBindingInterface<T> {
    fun bindData(item: T, view: View)
}