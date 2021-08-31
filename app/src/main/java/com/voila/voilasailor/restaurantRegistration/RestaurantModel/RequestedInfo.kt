package com.voila.voilasailor.restaurantRegistration.RestaurantModel

data class RequestedInfo(
    val ownerInfo: List<OwnerInfo>,
    val restaurantInfo: List<RestaurantInfo>,
    val restaurantProfilePic: List<RestaurantProfilePic>
)