package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

import com.voila.voilasailor.restaurantRegistration.RestaurantModel.FilterDish

data class GetAllDishWithFilterOptionResponse(
    val filterDish: List<FilterDish>,
    val message: String,
    val result: Boolean
)