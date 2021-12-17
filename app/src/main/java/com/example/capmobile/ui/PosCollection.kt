package com.example.capmobile.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentPosCollectionBinding
import com.example.capmobile.ui.interfaces.DrawerLocker

class PosCollection : Fragment() {
    private lateinit var binding: FragmentPosCollectionBinding
    private lateinit var backArrow: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pos_collection, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        // Disable the back from showing
        (activity as DrawerLocker).setDrawerEnabled(false)
    }

    private fun initializeViews() {
        backArrow = binding.imageButton
    }
}