package com.voila.voilasailor.restaurantRegistration.RestaurantModel

data class DocumentVerification(
    val document_name: String,
    val document_status: String,
    val is_verify: Boolean
)