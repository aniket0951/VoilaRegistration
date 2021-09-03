package com.voila.voilasailor.driverRegistration.ViewModelListener

interface DriverProfileViewModelListener {

    //on requested information
    fun onRequestedInformation()

    //on basic info
    fun onBasicInfo()

    fun onAddressInfo()

    fun onKYCInfo()

    fun onVehicleInfo()

    fun onVehicleDocuments()

    fun onBasicInfoUpdate()

    fun onAddressInfoUpdate()

    fun onVehicleInfoUpdate()

    fun onKYCInfoUpdate()

    fun onVehicleDocumentUpdate()
}