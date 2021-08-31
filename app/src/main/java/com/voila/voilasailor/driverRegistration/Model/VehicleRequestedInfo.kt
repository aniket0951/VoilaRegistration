package com.voila.voilasailor.driverRegistration.Model

data class VehicleRequestedInfo(
    val vehicle_RTO_registration_number: String,
    val vehicle_brand: String,
    val vehicle_colour: String,
    val vehicle_make_year: String,
    val vehicle_model: String,
    val vehicle_rc_number: String,
    val vehicle_type: String
)