package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

import com.voila.voilasailor.restaurantRegistration.RestaurantModel.Menu

data class GetMenusResponse(
    val menus: List<Menu>,
    val message: String,
    val result: Boolean
)