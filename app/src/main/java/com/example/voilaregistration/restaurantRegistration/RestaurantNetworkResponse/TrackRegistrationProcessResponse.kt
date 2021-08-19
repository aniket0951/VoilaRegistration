package com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse

import com.example.voilaregistration.restaurantRegistration.RestaurantModel.NeedToProcessComplete

data class TrackRegistrationProcessResponse(
        val message: String,
        val needToProcessComplete: List<NeedToProcessComplete>,
        val processCompleteStatus: String,
        val result: Boolean,
        val accountCreated : String
)