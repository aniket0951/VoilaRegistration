package com.example.voilaregistration.loginModule.Model

data class UserDetail(
    val api_token: String,
    val auth_token: String,
    val id: Int,
    val mobile_number: String
)