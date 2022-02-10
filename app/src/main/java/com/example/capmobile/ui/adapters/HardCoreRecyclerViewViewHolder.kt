package com.example.capmobile.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.capmobile.databinding.LayoutHardCoreItemBinding
import com.example.capmobile.data.model.HardCoreDataObjects

class HardCoreRecyclerViewViewHolder(val itemBinding: LayoutHardCoreItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(data: HardCoreDataObjects) {
        itemBinding.data = data
        itemBinding.executePendingBindings()
    }
}