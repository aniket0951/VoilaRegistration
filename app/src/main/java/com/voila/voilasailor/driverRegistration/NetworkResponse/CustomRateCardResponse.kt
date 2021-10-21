package com.voila.voilasailor.driverRegistration.NetworkResponse

import com.voila.voilasailor.driverRegistration.Model.OldRate
import com.voila.voilasailor.driverRegistration.Model.VehicleInfoRate

data class CustomRateCardResponse(
    val isRateCardSet: Boolean,
    val message: String,
    val oldRates: List<OldRate>,
    val result: Boolean,
    val vehicleInfoRate: List<VehicleInfoRate>
)