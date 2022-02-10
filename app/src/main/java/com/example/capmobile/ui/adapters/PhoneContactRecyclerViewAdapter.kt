package com.example.capmobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capmobile.databinding.PhoneContactRvLayoutBinding
import com.example.capmobile.data.model.PhoneContact

class PhoneContactRecyclerViewAdapter(
    private val listener: PhoneContactRecyclerViewClickListener
) :
    RecyclerView.Adapter<PhoneContactRecyclerViewAdapter.PhoneContactRvViewHolder>() {

    private var phoneContacts = arrayListOf<PhoneContact>()

    inner class PhoneContactRvViewHolder(private val itemBinding: PhoneContactRvLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(myData: PhoneContact) {
            itemBinding.apply {
                data = myData
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhoneContactRvViewHolder(
        PhoneContactRvLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PhoneContactRvViewHolder, position: Int) {
        holder.bind(phoneContacts[position])
        holder.itemView.setOnClickListener {
            listener.getClickedItem(phoneContacts[position])
        }
    }

    override fun getItemCount() = phoneContacts.size

    fun setData(myData: ArrayList<PhoneContact>) {
        phoneContacts = myData
    }

    interface PhoneContactRecyclerViewClickListener {
        fun getClickedItem(data: PhoneContact)
    }
}