package com.example.voilaregistration.loginModule.NetworkResponse

import com.example.voilaregistration.loginModule.Model.UserDetail

data class OtpVerificationResponse(
    val message: String,
    val result: Boolean,
    val userDetails: List<UserDetail>
)