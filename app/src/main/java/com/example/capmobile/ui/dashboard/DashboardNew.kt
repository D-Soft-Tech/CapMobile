package com.example.capmobile.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.DashboardBinding
import com.example.capmobile.ui.interfaces.DrawerLocker

class DashboardNew : Fragment() {
    private lateinit var binding: DashboardBinding
    private lateinit var accountSummary: CardView
    private lateinit var channelSummary: CardView
    private lateinit var cardFunding: CardView
    private lateinit var myDaoCode: CardView
    private lateinit var accounts: Button
    private lateinit var referAFriend: Button
    private lateinit var customerEngagement: Button
    private lateinit var drawerToggleIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        (activity as DrawerLocker).setDrawerEnabled(true)
        drawerToggleIcon.setOnClickListener {
            requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout).openDrawer(GravityCompat.START)
        }

        handleNavWithDashBoardButton()
    }

    private fun initializeViews() {
        accountSummary = binding.cardView3
        channelSummary = binding.cardView4
        cardFunding = binding.cardView5
        myDaoCode = binding.cardView6
        accounts = binding.button4
        referAFriend = binding.referAFriendBtn
        customerEngagement = binding.activateAccount
        drawerToggleIcon = binding.toggleIcon
    }

    private fun handleNavWithDashBoardButton() {
        accountSummary.cardViewClick(R.id.action_dashboardNew_to_accountStatus)
        referAFriend.cardViewClick(R.id.action_dashboardNew_to_referAFriendLandingPage)
        customerEngagement.cardViewClick(R.id.customerEngagement)
    }

    private fun View.cardViewClick(actionId: Int) {
        this.setOnClickListener {
            findNavController().navigate(actionId)
        }
    }
}