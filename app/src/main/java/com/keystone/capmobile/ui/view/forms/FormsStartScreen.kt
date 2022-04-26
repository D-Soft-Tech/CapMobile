package com.keystone.capmobile.ui.view.forms

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
import com.keystone.capmobile.databinding.FragmentFormsStartScreenBinding
import com.keystone.capmobile.ui.adapters.GenericRecyclerViewAdapter
import com.keystone.capmobile.ui.adapters.GenericSimpleRecyclerBindingInterface
import com.keystone.capmobile.ui.view.keyValueProducts.KeyValueProductsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FormsStartScreen @Inject constructor() : Fragment() {
    private lateinit var recyclerV: RecyclerView
    private lateinit var binding: FragmentFormsStartScreenBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_forms_start_screen,
            container,
            false
        )
        return binding.root
    }

}