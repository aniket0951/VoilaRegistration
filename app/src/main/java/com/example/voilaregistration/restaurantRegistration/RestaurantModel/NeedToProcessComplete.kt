package com.example.voilaregistration.restaurantRegistration.RestaurantModel

data class NeedToProcessComplete(
    val id: Int,
    val required_docs_name: String,
    val required_docs_type: String,
    val status: Int
)