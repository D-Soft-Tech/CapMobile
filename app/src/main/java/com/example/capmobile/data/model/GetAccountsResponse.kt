package com.example.capmobile.data.model

data class GetAccountsResponse(
    val details: List<Detail>,
    val message: String,
    val status: String,
    val token: Boolean
) {
    fun fromDtoToEntity() =
        details.map {
            with(it) {
                DaoChannelObject (
                    Name,
                    Phone_Number,
                    IsonMobile == 1,
                    IsonUSSD == 1,
                    IsOnIbank == 1,
                    Summary.contains("CARDED"),
                    STATUS
                )
            }
        }
}