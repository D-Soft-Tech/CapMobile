package com.keystone.capmobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.databinding.LayoutHardCoreItemBinding
import com.keystone.capmobile.data.model.HardCoreDataObjects
import com.keystone.capmobile.util.AppConstants.getBalloon
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HardCoreRecyclerViewAdapter @Inject constructor() : RecyclerView.Adapter<HardCoreRecyclerViewViewHolder>() {
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
                getBalloon(
                    it.context,
                    daoObjects[position].accountNumber,
                    it
                )
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