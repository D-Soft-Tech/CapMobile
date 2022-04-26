package com.keystone.capmobile.data.model

import android.os.Parcelable
import androidx.annotation.RawRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReactivateAcctBottomSheetData(
    var message: String,
    @RawRes var icon: Int,
    var actionId: Int? = null
): Parcelable