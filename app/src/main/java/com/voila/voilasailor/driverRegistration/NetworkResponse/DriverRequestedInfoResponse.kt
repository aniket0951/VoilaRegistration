package com.voila.voilasailor.driverRegistration.NetworkResponse

import com.voila.voilasailor.driverRegistration.Model.RequestedInfo

data class DriverRequestedInfoResponse(
    val message: String,
    val requestedInfo: List<RequestedInfo>,
    val result: Boolean
)