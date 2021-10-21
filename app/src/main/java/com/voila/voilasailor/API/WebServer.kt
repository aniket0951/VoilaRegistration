package com.voila.voilasailor.API

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

    //track driver registration process
    const val GET_TRACK_DRIVER_REGISTRATION = BASE_URL + "driverRegistrationProcess"

    //add basic information
    const val POST_ADD_PERSONAL_INFORMATION = BASE_URL + "addPersonalInformation"

    //add address details
    const val POST_ADD_ADDRESS_DETAILS = BASE_URL + "addAddressDetails"

    //add kyc docs
    const val POST_ADD_KYC_DOCUMENTS = BASE_URL + "addKYCDetails"

    //add vehicle information
    const val POST_ADD_VEHICLE_DETAILS = BASE_URL + "addDriverVehicleDetails"

    // add vehicle profile pictures
    const val POST_ADD_VEHICLE_PROFILE = BASE_URL +"addVehicleProfilePicture"

    //get all requested information
    const val GET_ALL_REQUESTED_INFORMATION = BASE_URL + "getAllRequestedInfo"

    //update driver personal information
    const val POST_UPDATE_PERSONAL_INFORMATION = BASE_URL + "updatePersonalInformation"

    //UPDATE address information
    const val POST_UPDATE_ADDRESS_INFORMATION = BASE_URL + "updateAddressInformation"

    //update vehicle information
    const val POST_UPDATE_VEHICLE_INFORMATION = BASE_URL + "updateVehicleInformation"

    //update kyc info
    const val POST_UPDATE_KYC_INFORMATION = BASE_URL + "updateKYCDetails"

    //update vehicle documents
    const val POST_UPDATE_VEHICLE_DOCUMENTS = BASE_URL + "updateVehicleDocument"

    /*-- get system rate card -- */
    const val GET_SYSTEM_RATE_CARD = BASE_URL + "getSystemRates"

    /*--- confirm a rate card ---*/
    const val  POST_CONFORM_RATE_CARD = BASE_URL + "createCustomeRateCard"

    /*-- get custom rate card --*/
    const val POST_CUSTOM_RATE_CARD = BASE_URL + "getDriverVehicleInfo"

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

    //update the menu
    const val POST_UPDATE_MENU = BASE_URL + "updateDishInfo"

    //remove the dish from restaurant menu card
    const val POST_REMOVE_DISH = BASE_URL + "removeDishFromRestaurant"

    //get restaurant profile requested information
    const val GET_RESTAURANT_REQUESTED_INFO = BASE_URL + "getAllRestaurantRequestedInfo"

    //update owner information
    const val POST_UPDATE_OWNER_DETAILS = BASE_URL + "updateResaturantOwnerInformation"

    //update restaurant details
    const val POST_UPDATE_RESTAURANT_DETAILS = BASE_URL + "updateRestaurantInformation"

    //update restaurant documents
    const val POST_UPDATE_RESTAURANT_DOCUMENT = BASE_URL + "updateRestaurantProfile"

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