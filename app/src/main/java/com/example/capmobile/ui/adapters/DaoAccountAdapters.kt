package com.example.capmobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capmobile.databinding.TableItemLayoutBinding
import com.example.capmobile.model.DaoChannelObject

class DaoAccountAdapters : RecyclerView.Adapter<DaoRecyclerViewHolder>() {
    private var listOfCustomersDaoDetails: List<DaoChannelObject> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DaoRecyclerViewHolder(
        TableItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DaoRecyclerViewHolder, position: Int) {
        holder.bind(listOfCustomersDaoDetails[position])
    }

    override fun getItemCount() = listOfCustomersDaoDetails.size

    fun setData(data: List<DaoChannelObject>) {
        listOfCustomersDaoDetails = data
    }
}