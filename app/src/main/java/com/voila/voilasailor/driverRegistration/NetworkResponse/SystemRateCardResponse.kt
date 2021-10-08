package com.voila.voilasailor.driverRegistration.NetworkResponse

import com.voila.voilasailor.driverRegistration.Model.SystemRate

data class SystemRateCardResponse(
    val message: String,
    val result: Boolean,
    val systemRates: List<SystemRate>
)