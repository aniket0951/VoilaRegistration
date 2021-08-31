package com.voila.voilasailor.driverRegistration.Model

data class RequestedInfo(
    val addressRequestedInfo: List<AddressRequestedInfo>,
    val basicRequestedInfo: List<BasicRequestedInfo>,
    val kycRequestedInfo: List<KycRequestedInfo>,
    val vehicleProfilePic: List<VehicleProfilePic>,
    val vehicleRequestedInfo: List<VehicleRequestedInfo>
)