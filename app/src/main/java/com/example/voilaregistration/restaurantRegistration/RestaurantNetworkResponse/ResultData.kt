package com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse

data class ResultData(
    val id: Int,
    val required_docs_name: String,
    val required_docs_type: String,
    val status: String,
    var editText : String,
    var autoCompleteTextView : String,
    var dishTypeSelected : String
)