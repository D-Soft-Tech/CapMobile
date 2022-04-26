package com.keystone.capmobile.data.model

data class GetLevelsAndDescription(
    val details: LevelDetails,
    val message: String,
    val status: String,
    val token: Boolean
) {
    fun fromDtoToEntity() =
        details.data
}