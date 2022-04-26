package com.keystone.capmobile.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.databinding.LayoutHardCoreItemBinding
import com.keystone.capmobile.data.model.HardCoreDataObjects

class HardCoreRecyclerViewViewHolder(val itemBinding: LayoutHardCoreItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(data: HardCoreDataObjects) {
        itemBinding.data = data
        itemBinding.executePendingBindings()
    }
}