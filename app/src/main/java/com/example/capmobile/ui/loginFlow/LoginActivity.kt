package com.example.capmobile.ui.loginFlow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capmobile.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CapMobile)
        setContentView(R.layout.activity_login)
    }
}