package com.voila.voilasailor.driverRegistration.Model

data class VehicleRequestedInfo(
    var vehicle_RTO_registration_number: String,
    val vehicle_brand: String,
    var vehicle_colour: String,
    var vehicle_make_year: String,
    val vehicle_model: String,
    var vehicle_rc_number: String,
    var vehicle_type: String,

    var vehicleRTORegistrationNumber:String,
    var vehicleBrand:String,
    var vehicleColor:String,
    var vehicleMakeYear:String,
)