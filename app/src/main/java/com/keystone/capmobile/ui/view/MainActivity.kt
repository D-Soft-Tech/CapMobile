package com.keystone.capmobile.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.keystone.capmobile.R
import com.keystone.capmobile.data.dataStore.AppProtoDataStore
import com.keystone.capmobile.databinding.ActivityMainBinding
import com.keystone.capmobile.databinding.LogoutDialogLayoutBinding
import com.keystone.capmobile.databinding.OpenAccountDialogLayoutBinding
import com.keystone.capmobile.databinding.ReferAFriendDialogLayoutBinding
import com.keystone.capmobile.ui.interfaces.DrawerLocker
import com.keystone.capmobile.ui.view.loginFlow.LoginActivity
import com.keystone.capmobile.util.AppConstants.createAnotherCustomizableDialog
import com.keystone.capmobile.util.AppConstants.getCustomDialog
import com.keystone.capmobile.util.AppConstants.getMomentOfTheDay
import com.keystone.capmobile.util.AppConstants.handleClickThatFirstShowDialog
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : AppCompatActivity(), DrawerLocker, StepperNavListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout

    @Inject
    lateinit var protoDataStore: AppProtoDataStore
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var pageNavView: NavigationView
    private lateinit var navController: NavController

    private lateinit var openAccountDialogBinding: OpenAccountDialogLayoutBinding
    private lateinit var openAccountDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // installSplashScreen()
        setTheme(R.style.Theme_CapMobile)
        setContentView(binding.root)

        // Initializing the layout bindings for the dialog
        openAccountDialogBinding = OpenAccountDialogLayoutBinding.inflate(layoutInflater)
        // Initializing the dialog objects
        openAccountDialog = createAnotherCustomizableDialog(openAccountDialogBinding.root)

        // Initialization of views
        initViews()

        navController = findNavController(R.id.fragment_container_view)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer)

        // customize the navigation drawer
        setUpNavDrawer()
        handleDestinationsWithNavDrawerMenuClick()
    }

    override fun onResume() {
        super.onResume()
        // Set Username to drawer header
        lifecycleScope.launch {
            setUsernameOnNavDrawerHeading()
        }
    }

    private fun initViews() {
        with(binding) {
            pageNavView = navView
            drawer = drawerLayout
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setUpNavDrawer() {
        pageNavView.setupWithNavController(navController )
        NavigationUI.setupWithNavController(pageNavView, navController)
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val locker =
            if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawer.setDrawerLockMode(locker)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun handleDestinationsWithNavDrawerMenuClick() {
        // selection
        pageNavView.setNavigationItemSelectedListener {

            // Dialog layout inflaters
            val view = LogoutDialogLayoutBinding.inflate(layoutInflater)
            val referAFriendView = ReferAFriendDialogLayoutBinding.inflate(layoutInflater)

            // Dialogs
            val logoutDialog = getCustomDialog(
                this,
                view.root
            ).apply {
                window?.setBackgroundDrawable(getDrawable(R.drawable.layout_page_with_curved_bg))
                window?.attributes?.windowAnimations = R.style.dialogAnimation
            }

            val referAFriendDialog = getCustomDialog(this, referAFriendView.root).apply {
                window?.setBackgroundDrawable(getDrawable(R.drawable.layout_page_with_curved_bg))
                window?.attributes?.windowAnimations = R.style.dialogAnimation
            }

            when (it.itemId) {
                R.id.log_out -> {

                    drawer.closeDrawer(GravityCompat.START)

                    view.noBtn.setOnClickListener {
                        logoutDialog.dismiss()
                    }

                    logoutDialog.show().apply {
                        view.yesBtn.setOnClickListener {
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            logoutDialog.dismiss()
                            finish()
                            startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }

                R.id.hard_core -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_hardCore)
                    return@setNavigationItemSelectedListener true
                }

                R.id.dashboardNew -> {
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }

                R.id.referAFriendLandingPage -> {
                    drawer.closeDrawer(GravityCompat.START)
                    referAFriendDialog.show().apply {
                        // When with gift is clicked, go the the refer a friend with gift page
                        referAFriendView.withGift.setOnClickListener {
                            referAFriendDialog.dismiss()
                            findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_referAFriendWithGiftScreen)
                        }
                        // When without gift is clicked, go the the refer a friend page
                        referAFriendView.withoutGift.setOnClickListener {
                            referAFriendDialog.dismiss()
                            findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_referAFriendWithoutGiftScreen)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }

                R.id.customerEngagement -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_customerEngagement)
                    return@setNavigationItemSelectedListener true
                }

                R.id.fragment_open_account -> {
                    drawer.closeDrawer(GravityCompat.START)
                    handleClickThatFirstShowDialog(
                        openAccountDialog,
                        openAccountDialogBinding.withBvn,
                        openAccountDialogBinding.withoutBVN,
                        {
                            findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_newAccountOpeningViewPager)
                        }
                    ) {
                        findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_openAccountWithoutBvnViewPager)
                    }
                    return@setNavigationItemSelectedListener true
                }

                R.id.myDaoAccounts -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_myDaoAccounts)
                    return@setNavigationItemSelectedListener true
                }

//                R.id.formsHomePage -> {
//                    drawer.closeDrawer(GravityCompat.START)
//                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_formsHomePage3)
//                    return@setNavigationItemSelectedListener true
//                }

                R.id.reactivateAccount2 -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_newReactivateAccountFragment)
                    return@setNavigationItemSelectedListener true
                }

                R.id.posCollection2 -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_collectionsHomePage2)
                    return@setNavigationItemSelectedListener true
                }

                R.id.keyValueProductsFragment -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_keyValueProductsFragment)
                    return@setNavigationItemSelectedListener true
                }

                R.id.accountStatus -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_cardFundingStatus)
                    return@setNavigationItemSelectedListener true
                }

                else -> {
                    return@setNavigationItemSelectedListener true
                }
            }
        }
    }

    override fun onCompleted() {
        Toast.makeText(this, getString(R.string.completed), Toast.LENGTH_SHORT).show()
    }

    override fun onStepChanged(step: Int) {
        if (step == 3) {
            Toast.makeText(this, getString(R.string.completed), Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun setUsernameOnNavDrawerHeading() {
        val header = pageNavView.getHeaderView(0)
        val navHeaderWelcomeTxt = header.findViewById<TextView>(R.id.headerWelcomeText)
        navHeaderWelcomeTxt.text
        protoDataStore.readProto.collect {
            navHeaderWelcomeTxt.text =
                getString(R.string.hi_user, getMomentOfTheDay(this), it.fullName.split(" ")[0])
        }
    }
}
