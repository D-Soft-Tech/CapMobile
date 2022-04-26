package com.keystone.capmobile.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GetBankBranchResponse(
    val BranchAddress: String,
    val BranchCode: String,
    val BranchName: String,
    val id: Int,
    val isActive: Boolean
): Parcelable