package com.example.capmobile.model

import android.graphics.drawable.Drawable

data class StatusCardDetails(
    var type: String,
    var icon: Int,
    var count: Int,
    var color: Int,
    var percent: Double?
)
