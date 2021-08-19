package com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse

data class GetAllRequiredDishDocsResponse(
    val message: String,
    val result: Boolean,
    val resultData: List<ResultData>
)