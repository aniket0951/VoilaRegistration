package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

data class GetAllRequiredDishDocsResponse(
    val message: String,
    val result: Boolean,
    val resultData: List<ResultData>
)