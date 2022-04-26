package com.keystone.capmobile.ui.view.forms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.KeyValueProductsModelClass
import com.keystone.capmobile.databinding.FragmentFormsHomePageBinding
import com.keystone.capmobile.ui.adapters.GenericRecyclerViewAdapter
import com.keystone.capmobile.ui.adapters.GenericSimpleRecyclerBindingInterface
import com.keystone.capmobile.ui.view.posCollection.CollectionsRvItems
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FormsHomePage @Inject constructor(): Fragment() {
    private lateinit var binding: FragmentFormsHomePageBinding
    private lateinit var dataToPassToAdapter: ArrayList<CollectionsRvItems>
    private lateinit var rv: RecyclerView
    private lateinit var rvAdapter: GenericRecyclerViewAdapter<KeyValueProductsModelClass>
    private var bindingInterface =
        object : GenericSimpleRecyclerBindingInterface<KeyValueProductsModelClass> {
            override fun bindData(item: KeyValueProductsModelClass, view: View) {
                val itemIcon = view.findViewById<ImageView>(R.id.itemIcon)
                val itemTitle = view.findViewById<TextView>(R.id.itemTitle)
                val goToDetailsIcon = view.findViewById<ImageView>(R.id.goToDetailsArrow)
                with(item) {
                    itemIcon.load(icon)
                    itemTitle.text = title
                }
//                goToDetailsIcon.setOnClickListener {
//                    val action =
//                        KeyValueProductsFragmentDirections.actionKeyValueProductsFragmentToKeyValueProductDetailFragment(
//                            item
//                        )
//                    findNavController().navigate(action)
//                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forms_home_page, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_forms_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initViews()
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
                getString(R.string.keyServe),
                R.id.action_collectionsHomePage2_to_posCollection2
            ),
            CollectionsRvItems(
                R.drawable.ic_key_swift,
                getString(R.string.keySwift),
                R.id.action_collectionsHomePage2_to_nqr
            ),
            CollectionsRvItems(
                R.drawable.ic_key_swift,
                getString(R.string.visaCardForm),
                R.id.action_collectionsHomePage2_to_nqr
            )
        )
        rvAdapter = GenericRecyclerViewAdapter(
            R.layout.collections_home_page_item_layout,
            bindingInterface
        ) {
            // Do nothing at the onClick of the view Holder
        }
//        rvAdapter.updateData(dataToPassToAdapter)
//        rv.adapter = rvAdapter
    }

    private fun initViews() {
        rv = binding.recyclerView
    }
}