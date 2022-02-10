package com.example.capmobile.ui.view.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.DashboardBinding
import com.example.capmobile.databinding.LogoutDialogLayoutBinding
import com.example.capmobile.databinding.OpenAccountDialogLayoutBinding
import com.example.capmobile.databinding.ReferAFriendDialogLayoutBinding
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.ui.view.loginFlow.LoginActivity
import com.example.capmobile.ui.viewModel.MainViewModel
import com.example.capmobile.util.AppConstants.createAnotherCustomizableDialog
import com.example.capmobile.util.AppConstants.getCustomDialog
import com.example.capmobile.util.AppConstants.handleClickThatFirstShowDialog
import com.example.capmobile.util.ConstantsOnly.USER_FIRST_NAME
import com.example.capmobile.util.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class DashboardNew : Fragment() {

    private lateinit var binding: DashboardBinding

    @Inject
    lateinit var dataStore: DataStoreManager
    private lateinit var accountSummary: CardView
    private lateinit var welcomeBackUser: TextView
    private lateinit var hardCore: CardView
    private lateinit var customerEngage: CardView
    private lateinit var myDaoCode: CardView
    private lateinit var openAccountButton: Button
    private lateinit var referAFriend: Button
    private lateinit var accountReactivation: Button
    private lateinit var drawerToggleIcon: ImageView
    private lateinit var openAccountDialog: AlertDialog
    private lateinit var referAFriendDialog: AlertDialog
    private val viewModel: MainViewModel by viewModels()
    private var token: String = "not found"

    // Layout inflaters for the dialog
    private lateinit var openAccountDialogBinding: OpenAccountDialogLayoutBinding
    private lateinit var referAFriendDialogBinding: ReferAFriendDialogLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard, container, false)

        // Initializing the layout bindings for the dialog
        openAccountDialogBinding = OpenAccountDialogLayoutBinding.inflate(layoutInflater)
        referAFriendDialogBinding = ReferAFriendDialogLayoutBinding.inflate(layoutInflater)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                customBackPressed()
            }
        })

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()

        // Initializing the dialog objects
        openAccountDialog = createAnotherCustomizableDialog(openAccountDialogBinding.root)
        referAFriendDialog = createAnotherCustomizableDialog(referAFriendDialogBinding.root)

        lifecycleScope.launch {
            dataStore.readStringFromDataStore(USER_FIRST_NAME).collect {
                welcomeBackUser.text = resources.getString(R.string.welcomeBack, it.split(" ")[0])
            }

//            viewModel.getToken().collect {
//                token = it
//            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onResume() {
        super.onResume()
        (activity as DrawerLocker).setDrawerEnabled(true)
        drawerToggleIcon.setOnClickListener {
            requireActivity().findViewById<DrawerLayout>(R.id.drawerLayout)
                .openDrawer(GravityCompat.START)
        }

        hardCore.setOnClickListener {
//            Toast.makeText(requireContext(), token , Toast.LENGTH_LONG).show()
//            dialog.show(childFragmentManager, "EngageIndividual")
        }

        handleNavWithDashBoardButton()

        referAFriend.handleClickThatFirstShowDialog(referAFriendDialog,
            referAFriendDialogBinding.withGift,
            referAFriendDialogBinding.withoutGift,
            { findNavController().navigate(R.id.action_dashboardNew_to_referAFriendWithGiftScreen) }) {
            findNavController().navigate(
                R.id.action_dashboardNew_to_referAFriendWithoutGiftScreen
            )
        }

        openAccountButton.handleClickThatFirstShowDialog(
            openAccountDialog,
            openAccountDialogBinding.withBvn,
            openAccountDialogBinding.withoutBVN,
            {
                findNavController().navigate(R.id.action_dashboardNew_to_newAccountOpeningViewPager)
            }
        ) {
            findNavController().navigate(R.id.action_dashboardNew_to_openAccountWithoutBvnViewPager)
        }
    }

    private fun initializeViews() {
        welcomeBackUser = binding.welcomeBackUser
        accountSummary = binding.cardView3
        hardCore = binding.cardView4
        customerEngage = binding.cardView5
        myDaoCode = binding.cardView6
        openAccountButton = binding.button4
        referAFriend = binding.referAFriendBtn
        accountReactivation = binding.activateAccount
        drawerToggleIcon = binding.toggleIcon
    }

    private fun handleNavWithDashBoardButton() {
        customerEngage.cardViewClick(R.id.action_dashboardNew_to_customerEngagement)
        accountSummary.cardViewClick(R.id.action_dashboardNew_to_cardFundingStatus)
        myDaoCode.cardViewClick(R.id.action_dashboardNew_to_myDaoAccounts)
        hardCore.cardViewClick(R.id.action_dashboardNew_to_hardCore)
        accountReactivation.cardViewClick(R.id.action_dashboardNew_to_reactivateAccountViewPager)
    }

    private fun View.cardViewClick(actionId: Int) {
        this.setOnClickListener {
            findNavController().navigate(actionId)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun customBackPressed() {

        // Logout Dialog layout inflaters
        val view = LogoutDialogLayoutBinding.inflate(layoutInflater)

        val logoutDialog = getCustomDialog(
            requireContext(),
            view.root
        ).apply {
            window?.setBackgroundDrawable(resources.getDrawable(R.drawable.layout_page_with_curved_bg))
            window?.attributes?.windowAnimations = R.style.dialogAnimation
        }

        view.noBtn.setOnClickListener {
            logoutDialog.dismiss()
        }

        logoutDialog.show().apply {
            view.yesBtn.setOnClickListener {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                logoutDialog.dismiss()
                requireActivity().finish()
                startActivity(intent)
            }
        }
    }
}