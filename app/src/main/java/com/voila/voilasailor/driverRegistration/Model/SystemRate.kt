package com.voila.voilasailor.driverRegistration.Model

data class SystemRate(
    val global_vehicle_id: Int,
    val max_rate: List<MaxRate>,
    val min_rate: List<MinRate>,
    val vehicle_RTO_registration_number: String,
    val vehicle_type: String
)