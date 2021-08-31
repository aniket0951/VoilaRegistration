package com.voila.voilasailor.restaurantRegistration.RestaurantModel

data class RestaurantInfo(
    val restaurant_close_time: String,
    val restaurant_contact_no: String,
    val restaurant_cuisines_type: String,
    val restaurant_email: String,
    val restaurant_establishment_year: String,
    val restaurant_name: String,
    val restaurant_opening_time: String,
    val restaurant_website: String,

    var restaurantName : String,
    var restaurantEmail : String,
    var restaurantContactNo : String,
    var restaurantOpeningTime : String,
    var restaurantClosingTime : String,
    var restaurantWebsite : String,
    var restaurantEstablishmentYear : String,
    var restaurantCuisinesType : String,
)