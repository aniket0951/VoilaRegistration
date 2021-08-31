package com.voila.voilasailor.driverRegistration.NetworkResponse

import com.voila.voilasailor.driverRegistration.Model.ResultData

data class AddPersonalInformationResponse(
    val message: String,
    val result: Boolean,
    val resultData: List<ResultData>
)