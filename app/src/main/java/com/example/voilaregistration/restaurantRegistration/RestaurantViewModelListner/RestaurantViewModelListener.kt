package com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner

interface RestaurantViewModelListener {

    fun onOwnerBasicDetailFound();
    fun onOwnerBasicDetailsNotFound(s: String);

    //add restaurant owner details
    fun onAddRestaurantOwnerDetailsSuccess()
    fun onAddRestaurantOwnerDetailsFailed(s: String)
}