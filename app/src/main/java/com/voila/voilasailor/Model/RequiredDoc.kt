package com.voila.voilasailor.Model

data class RequiredDoc(
        val driver_address_required_docs: List<DriverAddressRequiredDoc>,
        val driver_details_required_docs: List<DriverDetailsRequiredDoc>,
        val driver_kyc_required_docs: List<DriverKycRequiredDoc>,
        val driver_vehicle_required_docs: List<DriverVehicleRequiredDoc>
)