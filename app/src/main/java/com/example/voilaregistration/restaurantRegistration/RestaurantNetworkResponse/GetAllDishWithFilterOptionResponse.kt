package com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse

import com.example.voilaregistration.restaurantRegistration.RestaurantModel.FilterDish

data class GetAllDishWithFilterOptionResponse(
    val filterDish: List<FilterDish>,
    val message: String,
    val result: Boolean
)