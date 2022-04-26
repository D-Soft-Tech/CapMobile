package com.keystone.capmobile.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataToPassToDaoEngageDialog (
    var number: String,
    var name: String,
    var userEmailAddress: String,
    var locationOnScreen: IntArray? = null,
    var heightOfMenuIcon: Int
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataToPassToDaoEngageDialog

        if (number != other.number) return false
        if (locationOnScreen != null) {
            if (other.locationOnScreen == null) return false
            if (!locationOnScreen.contentEquals(other.locationOnScreen)) return false
        } else if (other.locationOnScreen != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + (locationOnScreen?.contentHashCode() ?: 0)
        return result
    }
}