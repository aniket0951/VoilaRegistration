package com.example.voilaregistration.API

object WebServer {

    //test remote url
    const val BASE_URL  = "http://3.7.18.55/api/"

    const val external_api_token = "WeweSJdhbbgfuysfgbkjnfakjsndfkajsdnlaksdadZASCXADA"


    //send otp
    const val POST_REGISTRATION_PROCESS_LOGIN = BASE_URL + "registrationProcessLogin"


    //verify the otp
    const val POST_VERITY_OTP = BASE_URL + "verifyOtp"


    /*-------------------------- DRIVER REGISTRATION MODULE ----------------------------- */






    /*-------------------------- RESTAURANT REGISTRATION MODULE ----------------------------- */

    //add new restaurant owner details
    const val POST_ADD_RESTAURANT_OWNER_DETAILS = BASE_URL + "addNewRestaurantOwner"


    //to trcak the registration process
    const val POST_TRACK_REGISTRATION_PROCESS = BASE_URL + "restaurantRegistrationProcess"


    /*-------------------------- REQUIRED DOCUMENTS ----------------------------- */

    //get all required docs
    const val POST_GET_ALL_REQUIRED_DOCS = BASE_URL + "getAllRequiredDocsToRegister"

}