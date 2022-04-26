package com.keystone.capmobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.LayoutHardCoreItemBinding
import com.keystone.capmobile.data.model.HardCoreDataObjects

class HardCoreAdapter2 : RecyclerView.Adapter<HardCoreRecyclerViewViewHolder>() {
    private var daoObjects: List<HardCoreDataObjects> = arrayListOf()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        HardCoreRecyclerViewViewHolder(
            LayoutHardCoreItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HardCoreRecyclerViewViewHolder, position: Int) {
        holder.apply {
            bind(daoObjects[position])
            itemBinding.accountName.setOnClickListener {
                it.findNavController().navigate(R.id.action_nqr_to_nqrScanPage)
            }
        }
    }

    override fun getItemCount() = daoObjects.size

    fun setData(newData: List<HardCoreDataObjects>) {
        val diff = RecyclerViewDiffUtil(newData, daoObjects)
        val diffResult = DiffUtil.calculateDiff(diff)
        daoObjects = newData
        diffResult.dispatchUpdatesTo(this)
    }
}