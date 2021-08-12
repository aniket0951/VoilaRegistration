package com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse

import com.example.voilaregistration.restaurantRegistration.RestaurantModel.ResultData

data class AddRestaurantOwnerDetailsResponse(
    val message: String,
    val result: Boolean,
    val resultData: List<ResultData>
)