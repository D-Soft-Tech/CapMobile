package com.keystone.capmobile.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keystone.capmobile.data.model.DaoChannelObject
import com.keystone.capmobile.data.model.DataToPassToDaoEngageDialog
import com.keystone.capmobile.databinding.TableItemLayoutBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

typealias Callback = (dataOfWhoToEngage: DataToPassToDaoEngageDialog) -> Unit

class DaoAccountAdapters @AssistedInject constructor(@Assisted private val onClick: Callback) :
    RecyclerView.Adapter<DaoRecyclerViewHolder>() {
    private var listOfCustomersDaoDetails: List<DaoChannelObject> = arrayListOf()
    private lateinit var parentRecyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        parentRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DaoRecyclerViewHolder(
        TableItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DaoRecyclerViewHolder, position: Int) {
        holder.bind(listOfCustomersDaoDetails[position])

        holder.itemViewBinding.menuItem.setOnClickListener {

            listOfCustomersDaoDetails[position]
            val location = IntArray(2)
            it.getLocationOnScreen(location)
            onClick(
                DataToPassToDaoEngageDialog(
                    listOfCustomersDaoDetails[position].number,
                    listOfCustomersDaoDetails[position].name,
                    listOfCustomersDaoDetails[position].emailAddress,
                    location,
                    it.height
                )
            )
        }
    }

    override fun getItemCount() = listOfCustomersDaoDetails.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<DaoChannelObject>) {
        listOfCustomersDaoDetails = data
        notifyDataSetChanged()
    }
}