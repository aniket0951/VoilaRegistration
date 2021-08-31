package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

import com.voila.voilasailor.restaurantRegistration.RestaurantModel.ResultData

data class AddRestaurantOwnerDetailsResponse(
    val message: String,
    val result: Boolean,
    val resultData: List<ResultData>
)