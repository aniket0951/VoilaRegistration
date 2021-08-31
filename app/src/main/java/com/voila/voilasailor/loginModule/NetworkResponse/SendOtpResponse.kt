package com.voila.voilasailor.loginModule.NetworkResponse

data class SendOtpResponse(
    val details: String,
    val message: String,
    val result: Boolean
)