package com.keystone.capmobile.data.model

import android.os.Parcelable
import androidx.annotation.RawRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModalData(
    @RawRes var icon: Int,
    var message: String
): Parcelable
