package com.example.capmobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capmobile.databinding.AccountSummaryItemLayoutBinding
import com.example.capmobile.model.StatusCardDetails

class StatusCardViewPagerAdapter :
    RecyclerView.Adapter<StatusCardViewPagerAdapter.ViewPagerHolder>() {

    private var listOfCardSummary: List<StatusCardDetails> = arrayListOf()

    class ViewPagerHolder(
        private val itemViewBinding: AccountSummaryItemLayoutBinding
    ) : RecyclerView.ViewHolder(itemViewBinding.root) {
        fun bind(data: StatusCardDetails) {
            itemViewBinding.apply {
                accountSummary = data
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder =
        ViewPagerHolder(
            AccountSummaryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.apply {
            bind(listOfCardSummary[position])
        }
    }

    override fun getItemCount() = listOfCardSummary.size

    fun setData(statusCards: List<StatusCardDetails>) {
        listOfCardSummary = statusCards
    }

    fun getPageWidth(position: Float): Float {
        return 0.8f
    }
}