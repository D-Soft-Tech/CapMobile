package com.keystone.capmobile.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.databinding.TableItemLayoutBinding
import com.keystone.capmobile.data.model.DaoChannelObject

class DaoRecyclerViewHolder(val itemViewBinding: TableItemLayoutBinding) : RecyclerView.ViewHolder(itemViewBinding.root) {
    fun bind(dataFromBackend: DaoChannelObject) {
        itemViewBinding.data = dataFromBackend
        itemViewBinding.executePendingBindings()
    }
}
