package com.example.capmobile.utill

import android.widget.Button
import androidx.navigation.findNavController
import com.example.capmobile.R

object AppConstant {

    fun Button.navigateToDashBoard() {
        this.findNavController().navigate(
            R.id.action_loginPage_to_dashBoard
        )
    }
}
