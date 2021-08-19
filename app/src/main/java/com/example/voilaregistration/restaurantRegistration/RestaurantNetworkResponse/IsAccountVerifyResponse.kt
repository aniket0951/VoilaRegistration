package com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse

data class IsAccountVerifyResponse(
    val message: String,
    val result: Boolean,
    val isVerify : Boolean,
    val verificationData : String
)