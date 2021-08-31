package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

import com.voila.voilasailor.restaurantRegistration.RestaurantModel.FilterOption

data class GetAllFilterOptionResponse(
    val filterOptions: List<FilterOption>,
    val message: String,
    val result: Boolean
)