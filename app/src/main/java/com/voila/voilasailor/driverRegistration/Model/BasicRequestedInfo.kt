package com.voila.voilasailor.driverRegistration.Model

data class BasicRequestedInfo(
    var contact_number: String,
    var date_of_birth: String,
    var email: String,
    var full_name: String,

    val fullName: String,
    val emails:String,
    val dateOfBirth:String,
    val contactNumber:String
)