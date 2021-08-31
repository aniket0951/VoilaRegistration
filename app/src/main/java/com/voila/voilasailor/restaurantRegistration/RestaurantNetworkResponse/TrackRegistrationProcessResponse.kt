package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

import com.voila.voilasailor.restaurantRegistration.RestaurantModel.NeedToProcessComplete

data class TrackRegistrationProcessResponse(
        val message: String,
        val needToProcessComplete: List<NeedToProcessComplete>,
        val processCompleteStatus: String,
        val result: Boolean,
        val accountCreated : String
)