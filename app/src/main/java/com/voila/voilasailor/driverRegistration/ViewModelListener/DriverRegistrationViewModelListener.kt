package com.voila.voilasailor.driverRegistration.ViewModelListener

interface DriverRegistrationViewModelListener {

    //on track successfully
    fun onDriverRegistrationTrackSuccessfully()

    //on add personal information success
    fun onAddPersonalInformationSuccess()

    //on add address successfully
    fun onAddAddressSuccessfully()

    //on kys doc uploaded successfully
    fun onKYCDocumentUploadedSuccessfully()

    //on vehicle details added successfully
    fun onAddVehicleDetailsSuccessfully()

    //on vehicle profile added successfully
    fun onVehicleProfileUploadedSuccessfully()
}