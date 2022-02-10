package com.example.capmobile.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataToPassToDaoEngageDialog (
    var phoneNumber: String,
    var name: String,
    var locationOnScreen: IntArray? = null,
    var heightOfMenuIcon: Int
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataToPassToDaoEngageDialog

        if (phoneNumber != other.phoneNumber) return false
        if (locationOnScreen != null) {
            if (other.locationOnScreen == null) return false
            if (!locationOnScreen.contentEquals(other.locationOnScreen)) return false
        } else if (other.locationOnScreen != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = phoneNumber.hashCode()
        result = 31 * result + (locationOnScreen?.contentHashCode() ?: 0)
        return result
    }
}