package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

import com.voila.voilasailor.restaurantRegistration.RestaurantModel.TrcakData

data class RestaurantVerificationTrackResponse(
    val message: String,
    val result: Boolean,
    val trcak_data: List<TrcakData>
)