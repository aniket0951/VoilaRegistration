package com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner

interface UpdateMenuViewModelListener {

    fun onFailed(string: String)
    fun onSuccess(string: String)

    //on menu update with image
    fun onMenuUpdateWithImage()

    //on menu update
    fun onMenuUpdate()

}