package com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse

import com.example.voilaregistration.restaurantRegistration.RestaurantModel.Menu

data class GetMenusResponse(
    val menus: List<Menu>,
    val message: String,
    val result: Boolean
)