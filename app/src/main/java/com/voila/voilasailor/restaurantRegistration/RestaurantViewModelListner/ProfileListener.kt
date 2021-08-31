package com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner

interface ProfileListener {

   fun onSuccess(s:String)
   fun onFailed(s:String)

   //on owner details
   fun onOwnerDetailsSuccess()

   //on restaurant details
   fun onRestaurantDetailsSuccess()

   //on restaurant docs details
   fun onRestaurantDocsDetailsSuccess()

   //on owner details update successfully
   fun onOwnerDetailsUpdateSuccessfully()

   //on restaurant details update successfully
   fun onRestaurantDetailsUpdateSuccessfully()

   //on restaurant docs details update successfully
   fun onRestaurantDocumentUpdateSuccessfully()
}