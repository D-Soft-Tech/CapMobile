package com.example.capmobile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.capmobile.R
import com.example.capmobile.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawer: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolBar: Toolbar
//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        installSplashScreen()
        setTheme(R.style.Theme_CapMobile)
        setContentView(binding.root)

        // Initialization of views
        drawer = binding.drawerLayout
        navView = binding.navView
        toolBar = binding.appToolBar.toolBar
        navController = findNavController(R.id.fragment_container_view)

//        setupActionBarWithNavController(navController, drawer)
    }
}
