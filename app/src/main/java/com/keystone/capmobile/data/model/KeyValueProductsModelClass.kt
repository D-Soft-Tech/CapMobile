package com.keystone.capmobile.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeyValueProductsModelClass(
    var icon: Int,
    var title: String,
    var hasOnlyFlyer: Boolean,
    var flyer: Int?,
    var movieUrl: String,
    var onBoardingSteps: List<String>
): Parcelable