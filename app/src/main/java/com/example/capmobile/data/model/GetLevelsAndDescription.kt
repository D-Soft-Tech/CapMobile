package com.example.capmobile.data.model

data class GetLevelsAndDescription(
    val details: LevelDetails,
    val message: String,
    val status: String,
    val token: Boolean
) {
    fun fromDtoToEntity() =
        details.data.map { it.LevelName }
}