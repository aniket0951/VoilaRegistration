package com.voila.voilasailor.restaurantRegistration.RestaurantModel

data class InformationVerification(
    val DocumentVerification: List<DocumentVerification>,
    val document_name: String,
    val document_status: String,
    val is_sub_doc: Boolean,
    val is_verify: Boolean
)