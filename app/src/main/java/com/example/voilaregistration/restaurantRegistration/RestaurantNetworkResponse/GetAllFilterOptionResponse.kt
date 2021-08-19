package com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse

import com.example.voilaregistration.restaurantRegistration.RestaurantModel.FilterOption

data class GetAllFilterOptionResponse(
    val filterOptions: List<FilterOption>,
    val message: String,
    val result: Boolean
)