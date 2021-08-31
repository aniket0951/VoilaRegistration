package com.voila.voilasailor.driverRegistration.Model

data class KycRequestedInfo(
    val aadhar_back_photo: String,
    val aadhar_front_photo: String,
    val licence_back_photo: String,
    val licence_front_photo: String,
    val pan_card: String,
    val passport_size_photo: String
)