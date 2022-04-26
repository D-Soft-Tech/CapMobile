package com.keystone.capmobile.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.keystone.capmobile.data.model.HardCoreDataObjects

class RecyclerViewDiffUtil(
    private val newList: List<HardCoreDataObjects>,
    private val oldList: List<HardCoreDataObjects>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].accountNumber == newList[newItemPosition].accountNumber ||
                oldList[oldItemPosition].name == newList[newItemPosition].name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].accountNumber == newList[newItemPosition].accountNumber ||
                oldList[oldItemPosition].name == newList[newItemPosition].name
}