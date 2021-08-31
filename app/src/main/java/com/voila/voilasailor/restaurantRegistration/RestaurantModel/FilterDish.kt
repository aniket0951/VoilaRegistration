package com.voila.voilasailor.restaurantRegistration.RestaurantModel

data class FilterDish(
    val created_at: String,
    val dish_description: String,
    val dish_item: String,
    val dish_name: String,
    val dish_photo: String,
    val dish_price: String,
    val dish_type: String,
    val id: Int,
    val restaurant_id: Int,
    val restaurant_token_id: String,
    val updated_at: String,
    val menuUrl : String
)