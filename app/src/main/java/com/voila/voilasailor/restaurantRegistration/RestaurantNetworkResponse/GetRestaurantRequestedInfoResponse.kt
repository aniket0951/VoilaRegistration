package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

import com.voila.voilasailor.restaurantRegistration.RestaurantModel.RequestedInfo

data class GetRestaurantRequestedInfoResponse(
    val message: String,
    val requestedInfo: List<RequestedInfo>,
    val result: Boolean
)