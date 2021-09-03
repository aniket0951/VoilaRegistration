package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

data class DummyTrackResponse(
    val message: String,
    val needToProcessComplete: List<NeedToProcessComplete>,
    val processComplete: String,
    val result: Boolean
)