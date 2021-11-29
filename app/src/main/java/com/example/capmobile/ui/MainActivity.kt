package com.example.capmobile.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolBar: Toolbar
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
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
        toolBar = binding.appToolBar.toolBar
        navController = findNavController(R.id.fragment_container_view)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.dashBoard), drawer)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolBar,
            R.string.nav_app_bar_open_drawer_description,
            R.string.nav_app_bar_close_drawer_description
        )

        // customize the navigation drawer
        setUpNavDrawer()

        // selection
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.customerEngagement -> {
                    return@setNavigationItemSelectedListener true
                }
                else -> {
                    return@setNavigationItemSelectedListener true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setUpNavDrawer() {
        drawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        actionBarDrawerToggle.syncState()
        setSupportActionBar(toolBar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_drawer_menu)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setDestination() {}
}
