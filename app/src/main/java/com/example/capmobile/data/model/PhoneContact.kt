package com.example.capmobile.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneContact(
    var name: String,
    var phoneNumber: String,
    var thumbNail: String? = null
) : Parcelable
