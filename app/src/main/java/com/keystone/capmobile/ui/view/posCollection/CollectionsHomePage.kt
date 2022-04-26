package com.keystone.capmobile.ui.view.posCollection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentCollectionsHomePageBinding
import com.keystone.capmobile.ui.adapters.GenericRecyclerViewAdapter
import com.keystone.capmobile.ui.adapters.GenericSimpleRecyclerBindingInterface
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CollectionsHomePage @Inject constructor() : Fragment() {
    private lateinit var backArrowImageView: ImageView
    private lateinit var backTextView: TextView
    private lateinit var recyclerV: RecyclerView
    private lateinit var binding: FragmentCollectionsHomePageBinding
    private lateinit var recyclerViewAdapter: GenericRecyclerViewAdapter<CollectionsRvItems>
    private var bindingInterface =
        object : GenericSimpleRecyclerBindingInterface<CollectionsRvItems> {
            override fun bindData(item: CollectionsRvItems, view: View) {
                val itemIcon = view.findViewById<ImageView>(R.id.itemIcon)
                val itemTitle = view.findViewById<TextView>(R.id.itemTitle)
                val goToDetailsIcon = view.findViewById<ImageView>(R.id.goToDetailsArrow)
                with(item) {
                    itemIcon.load(icon)
                    itemTitle.text = title
                }
                goToDetailsIcon.setOnClickListener {
                    findNavController().navigate(item.destinationAction)
                }
            }
        }
    private lateinit var dataToPassToAdapter: ArrayList<CollectionsRvItems>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_collections_home_page,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        recyclerViewAdapter = GenericRecyclerViewAdapter(
            R.layout.collections_home_page_item_layout,
            bindingInterface
        ) {
            // I left this empty, because it is not on the whole item view i want to set the click listener
            // I put the click listener on the arrow in the item layout
        }
    }

    override fun onResume() {
        super.onResume()
        dataToPassToAdapter = arrayListOf(
            CollectionsRvItems(
                R.drawable.ic_fontisto_shopping_pos_machine,
                getString(R.string.posHeading),
                R.id.action_collectionsHomePage2_to_posCollection2
            ),
            CollectionsRvItems(
                R.drawable.ic_bi_qr_code_scan__1_,
                getString(R.string.nqr),
                R.id.action_collectionsHomePage2_to_nqr
            ),
            CollectionsRvItems(
                R.drawable.ic_ussd,
                getString(R.string.portals),
                R.id.action_collectionsHomePage2_to_posCollection2
            ),
            CollectionsRvItems(
                R.drawable.ic_onboard_customer,
                getString(R.string.onboard_merchant),
                R.id.action_collectionsHomePage2_to_nqr
            )
        )
        backTextView.setOnClickListener {
            findNavController().popBackStack()
        }
        backArrowImageView.setOnClickListener {
            findNavController().popBackStack()
        }
        recyclerViewAdapter.updateData(dataToPassToAdapter)
        recyclerV.adapter = recyclerViewAdapter
    }

    private fun initViews() {
        with(binding) {
            backArrowImageView = backArrow
            backTextView = backText
            recyclerV = recyclerView
        }
    }
}