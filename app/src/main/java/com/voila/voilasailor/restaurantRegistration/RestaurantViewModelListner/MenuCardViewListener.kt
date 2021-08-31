package com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner

interface MenuCardViewListener {

    fun onSuccess(string: String)
    fun onFailed(string: String)

    /*-- to move restaurant home screen -- */
    fun onMoveHomeScreen()

    //get required docs
    fun onGetRequiredDishDocsSuccess()

    //on dish added successfully
    fun onDishAddedSuccessfully()
}