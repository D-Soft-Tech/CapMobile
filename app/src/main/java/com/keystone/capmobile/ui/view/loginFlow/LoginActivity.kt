package com.keystone.capmobile.ui.view.loginFlow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.keystone.capmobile.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity @Inject constructor() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_CapMobile)
        setContentView(R.layout.activity_login)
    }
}