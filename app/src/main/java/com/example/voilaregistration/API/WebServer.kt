package com.example.voilaregistration.API

object WebServer {

    //local host url
   // const val BASE_URL = "http://192.168.0.183:80/api/"

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

    //add restaurant profile details
    const val POST_ADD_RESTAURANT_PROFILE_DETAILS = BASE_URL + "addNewRestaurantProfileInfo"

    //add restaurant profile photo
    const val POST_ADD_RESTAURANT_PROFILE_PHOTO = BASE_URL + "addRestaurantProfile"

    //to trcak the registration process
    const val POST_TRACK_REGISTRATION_PROCESS = BASE_URL + "restaurantRegistrationProcess"

    //to check a account is verify or not
    const val GET_IS_ACCOUNT_VERIFY = BASE_URL + "isRestaurantAccountVerify"

    //to add new dish/menu
    const val POST_ADD_NEW_DISH = BASE_URL + "addNewDish"

    //get all menus
    const val GET_ALL_MENUS =  BASE_URL + "getAllMenus"


    /*------------------------ FILTER OPTIONS -----------------*/

    //get filter options
    const val GET_ALL_FILTER_OPTIONS = BASE_URL + "getAllFilterOption"

    //get selected filter type dish
    const val POST_GET_DISH_WITH_FILTER = BASE_URL + "getDishWithFilter"


    /*-------------------------- REQUIRED DOCUMENTS ----------------------------- */

    //get all required docs
    const val POST_GET_ALL_REQUIRED_DOCS = BASE_URL + "getAllRequiredDocsToRegister"

    //get all required docs for dish
    const val GET_ALL_REQUIRED_DISH_DOCS = BASE_URL + "getDishRequiredDocs"
}