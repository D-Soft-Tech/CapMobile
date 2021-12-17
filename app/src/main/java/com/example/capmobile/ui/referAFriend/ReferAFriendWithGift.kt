package com.example.capmobile.ui.referAFriend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentReferAFriendWithGiftBinding
import com.example.capmobile.ui.interfaces.DrawerLocker

class ReferAFriendWithGift : Fragment() {
    private lateinit var backArrow: ImageView
    private lateinit var binding: FragmentReferAFriendWithGiftBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_refer_a_friend_with_gift,
            container,
            false
        )
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

        // Disable drawer from showing
        (activity as DrawerLocker).setDrawerEnabled(false)
    }

    private fun initializeViews() {
        backArrow = binding.imageButton
    }
}