package com.voila.voilasailor.driverRegistration.Model

data class AddressRequestedInfo(
    val building_name: String,
    val district: String,
    val house_number: String,
    val landmark: String,
    val pin_code: String,
    val state: String,
    val street_name: String
)