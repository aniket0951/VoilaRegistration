package com.voila.voilasailor.driverRegistration.NetworkResponse

import com.voila.voilasailor.restaurantRegistration.RestaurantModel.NeedToProcessComplete

data class TrackDriverRegistrationProccessResponse(
    val message: String,
    val needToProcessComplete: List<NeedToProcessComplete>,
    val processCompleteStatus: String,
    val result: Boolean,
    val isVerify : Boolean,
    val verificationData :  String
)