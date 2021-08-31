package com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse

data class IsAccountVerifyResponse(
    val message: String,
    val result: Boolean,
    val isVerify : Boolean,
    val verificationData : String
)