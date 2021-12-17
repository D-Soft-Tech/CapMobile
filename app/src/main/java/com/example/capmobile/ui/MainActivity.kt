package com.example.capmobile.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.ActivityMainBinding
import com.example.capmobile.databinding.LogoutDialogLayoutBinding
import com.example.capmobile.ui.interfaces.DrawerLocker
import com.example.capmobile.ui.loginFlow.LoginActivity
import com.example.capmobile.util.AppConstants.getCustomDialog
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), DrawerLocker {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var navView: NavigationView

    //    private lateinit var toolBar: Toolbar
//    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // installSplashScreen()
        setTheme(R.style.Theme_CapMobile)
        setContentView(binding.root)

        // Initialization of views
        drawer = binding.drawerLayout
        navView = binding.navView
//        toolBar = binding.appToolBar.toolBar
        navController = findNavController(R.id.fragment_container_view)
//        appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.fragment_open_account, R.id.dashBoard
//        ), drawer)


        // customize the navigation drawer
        setUpNavDrawer()
        handleDestinationsWithNavDrawerMenuClick()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setUpNavDrawer() {
        navView.setupWithNavController(navController)
//        setSupportActionBar(toolBar)
//        actionBarDrawerToggle = ActionBarDrawerToggle(
//            this,
//            drawer,
//            toolBar,
//            R.string.nav_app_bar_open_drawer_description,
//            R.string.nav_app_bar_close_drawer_description
//        )
//        actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
//        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
//        supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
//            setDisplayShowTitleEnabled(false)
////            setHomeAsUpIndicator(R.drawable.ic_drawer_menu)
//        }
//        drawer.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()
//
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val locker =
            if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawer.setDrawerLockMode(locker)
    }


    private fun handleDestinationsWithNavDrawerMenuClick() {
        // selection
        navView.setNavigationItemSelectedListener {

            val view = LogoutDialogLayoutBinding.inflate(layoutInflater)

            when (it.itemId) {
                R.id.log_out -> {
                    val logoutDialog = getCustomDialog(
                        this,
                        view.root
                    )

                    drawer.closeDrawer(GravityCompat.START)

                    logoutDialog.show().apply {
                        view.noBtn.setOnClickListener {
                            dismiss()
                        }
                        view.yesBtn.setOnClickListener {
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            dismiss()
                            finish()
                            startActivity(intent)
                        }
                    }
                    return@setNavigationItemSelectedListener true
                }

                R.id.dashboardNew -> {
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }

                R.id.referAFriendLandingPage -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_referAFriendLandingPage)
                    return@setNavigationItemSelectedListener true
                }

                R.id.customerEngagement -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_customerEngagement)
                    return@setNavigationItemSelectedListener true
                }

                R.id.fragment_open_account -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_fragment_open_account)
                    return@setNavigationItemSelectedListener true
                }

                R.id.myDaoAccounts -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_myDaoAccounts)
                    return@setNavigationItemSelectedListener true
                }

                R.id.reactivateAccount2 -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_reactivateAccount2)
                    return@setNavigationItemSelectedListener true
                }

                R.id.posCollection2 -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_posCollection2)
                    return@setNavigationItemSelectedListener true
                }

                R.id.accountStatus -> {
                    drawer.closeDrawer(GravityCompat.START)
                    findNavController(R.id.fragment_container_view).navigate(R.id.action_dashboardNew_to_accountStatus)
                    return@setNavigationItemSelectedListener true
                }

                else -> {
                    return@setNavigationItemSelectedListener true
                }
            }
        }
    }
}
