package com.keystone.capmobile.ui.view.referAFriend

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.keystone.capmobile.R
import com.keystone.capmobile.databinding.FragmentReferAFriendWithoutGiftScreenBinding
import com.keystone.capmobile.ui.interfaces.DrawerLocker
import com.keystone.capmobile.util.AppConstants.copyToClipBoard

class ReferAFriendWithoutGiftScreen : Fragment() {
    private lateinit var binding: FragmentReferAFriendWithoutGiftScreenBinding
    private lateinit var copyBtn: Button
    private lateinit var shareBtn: Button
    private lateinit var backArrow: ImageView
    private lateinit var backText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_refer_a_friend_without_gift_screen,
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

        (activity as DrawerLocker).setDrawerEnabled(false)

        backText.setOnClickListener {
            findNavController().popBackStack()
        }

        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        copyBtn.setOnClickListener {
            copyToClipBoard(requireContext(), copyBtn.text.toString(), getString(R.string.referalCode))
        }

        shareBtn.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.refer_a_friend_without_gift_msg))
            }
            startActivity(intent)
        }
    }

    private fun initViews() {
        copyBtn = binding.codeCopyBtn
        shareBtn = binding.shareBtn
        backArrow = binding.backArrow
        backText = binding.backText
    }
}