package com.example.capmobile.ui.referAFriend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.FragmentReferAFriendLandingPageBinding
import com.example.capmobile.ui.interfaces.DrawerLocker

class ReferAFriendLandingPage : Fragment() {
    private lateinit var binding: FragmentReferAFriendLandingPageBinding
    private lateinit var referAFriendWithGiftBtn: Button
    private lateinit var backArrow: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_refer_a_friend_landing_page, container, false)
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

        referAFriendWithGiftBtn.setOnClickListener {
            findNavController().navigate(R.id.action_referAFriendLandingPage_to_referAFriendWithGift2)
        }

        // Disable drawer from showing
        (activity as DrawerLocker).setDrawerEnabled(false)
    }

    private fun initializeViews() {
        referAFriendWithGiftBtn = binding.referAFriendWithGift
        backArrow = binding.imageButton
    }
}