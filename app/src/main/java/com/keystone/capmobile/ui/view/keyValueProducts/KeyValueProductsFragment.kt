package com.keystone.capmobile.ui.view.keyValueProducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.keystone.capmobile.R
import com.keystone.capmobile.data.model.KeyValueProductsModelClass
import com.keystone.capmobile.databinding.FragmentKeyValueProductsBinding
import com.keystone.capmobile.ui.adapters.GenericRecyclerViewAdapter
import com.keystone.capmobile.ui.adapters.GenericSimpleRecyclerBindingInterface
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KeyValueProductsFragment @Inject constructor() : Fragment() {
    private lateinit var backArrowImageView: ImageView
    private lateinit var backTextView: TextView
    private lateinit var recyclerV: RecyclerView
    private lateinit var binding: FragmentKeyValueProductsBinding
    private lateinit var recyclerViewAdapter: GenericRecyclerViewAdapter<KeyValueProductsModelClass>
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
                goToDetailsIcon.setOnClickListener {
                    val action =
                        KeyValueProductsFragmentDirections.actionKeyValueProductsFragmentToKeyValueProductDetailFragment(
                            item
                        )
                    findNavController().navigate(action)
                }
            }
        }
    private lateinit var dataToPassToAdapter: ArrayList<KeyValueProductsModelClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_key_value_products,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        dataToPassToAdapter = arrayListOf(
            KeyValueProductsModelClass(
                R.drawable.ic_fontisto_shopping_pos_machine,
                getString(R.string.posHeading),
                false,
                null,
                "https://www.youtube.com/embed/Oxev5tFtWyQ?feature=oembed&amp;wmode=opaque",
                resources.getStringArray(R.array.pos_on_boarding_steps).toList()
            ),
            KeyValueProductsModelClass(
                R.drawable.ic_bi_qr_code_scan__1_,
                getString(R.string.nqr),
                true,
                R.drawable.nqr_onboarding_flyer,
                "https://www.youtube.com/embed/Oxev5tFtWyQ?feature=oembed&amp;wmode=opaque",
                resources.getStringArray(R.array.pos_on_boarding_steps).toList()
            ),
            KeyValueProductsModelClass(
                R.drawable.ic_ussd,
                getString(R.string.ussd_without_collon),
                false,
                null,
                "https://www.youtube.com/embed/cN2xE2rm3Lg",
                resources.getStringArray(R.array.ussd_on_boarding_steps).toList()
            ),
            KeyValueProductsModelClass(
                R.drawable.ic_key_swift,
                getString(R.string.keySwift),
                false,
                null,
                "https://www.youtube.com/embed/pyaZOdRp33k",
                resources.getStringArray(R.array.key_mobile_on_boarding_steps).toList()
            ),
            KeyValueProductsModelClass(
                R.drawable.ic_internet_banking,
                getString(R.string.internetBanking),
                false,
                null,
                "https://www.youtube.com/embed/pyaZOdRp33k",
                resources.getStringArray(R.array.internet_banking_on_boarding_steps).toList()
            ),
            KeyValueProductsModelClass(
                R.drawable.ic_internet_banking,
                getString(R.string.mToken),
                false,
                null,
                "https://www.youtube.com/embed/pyaZOdRp33k",
                resources.getStringArray(R.array.m_token_on_boarding_steps).toList()
            )
        )
        backTextView.setOnClickListener {
            findNavController().popBackStack()
        }
        backArrowImageView.setOnClickListener {
            findNavController().popBackStack()
        }
        recyclerViewAdapter = GenericRecyclerViewAdapter(R.layout.collections_home_page_item_layout, bindingInterface) {
            // Do nothing
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