package com.voila.voilasailor.loginModule.Model

data class UserDetail(
    val api_token: String,
    val auth_token: String,
    val id: Int,
    val mobile_number: String
)