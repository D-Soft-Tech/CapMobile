package com.example.capmobile.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.capmobile.databinding.TableItemLayoutBinding
import com.example.capmobile.model.DaoChannelObject

class DaoRecyclerViewHolder(private val itemViewBinding: TableItemLayoutBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {
    fun bind(dataFromBackend: DaoChannelObject) {
        itemViewBinding.data = dataFromBackend
        itemViewBinding.executePendingBindings()
    }
}
