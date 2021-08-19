package com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner

interface RestaurantHomeListener {

    //on success
    fun onSuccess(s:String)

    //on failed
    fun onFailed(s: String)

    //on is account verify success
    fun onIsAccountVerify()

    //on get all menu
    fun onGetAllMenus()

    //on get all filter options
    fun onGetFilterOptions()

    //on filter apply successfully
    fun onFilterApplySuccessfully()
}