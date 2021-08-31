package com.voila.voilasailor.loginModule.NetworkResponse

import com.voila.voilasailor.loginModule.Model.UserDetail

data class OtpVerificationResponse(
    val message: String,
    val result: Boolean,
    val userDetails: List<UserDetail>
)