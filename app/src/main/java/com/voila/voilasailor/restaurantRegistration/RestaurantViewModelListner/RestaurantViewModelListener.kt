package com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner

interface RestaurantViewModelListener {

    fun onSuccess(s: String)
    fun onFailed(s: String)

    fun onOwnerBasicDetailFound();

    //add restaurant owner details
    fun onAddRestaurantOwnerDetailsSuccess()

    //track registration process
    fun toTrackRegistrationProcessSuccess()

    //add restaurant profile details
    fun onAddRestaurantProfileDetailsSuccess()

    //add restaurant profile photo
    fun onAddRestaurantPhotoSuccess()

}