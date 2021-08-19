package com.example.voilaregistration.API

import com.example.voilaregistration.NetworkResponse.GetAllRequiredDocsResponse
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import com.example.voilaregistration.loginModule.NetworkResponse.OtpVerificationResponse
import com.example.voilaregistration.loginModule.NetworkResponse.SendOtpResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.*
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    /*--------------------  TO GET ALL REQUIRED DOCS ----------------------------*/
    @POST(WebServer.POST_GET_ALL_REQUIRED_DOCS)
    fun getAllRequiredDocs(@Query("title") title: String?): Observable<GetAllRequiredDocsResponse?>?

    @POST(WebServer.POST_GET_ALL_REQUIRED_DOCS)
    fun getAllRequiredRestaurantDocs(@Query("title") title: String?): Observable<GetAllRestaurantDocsResponse?>?

    //get dish required docs
    @GET(WebServer.GET_ALL_REQUIRED_DISH_DOCS)
    fun getAllRequiredDishDocs(@Query("api_token")api_token: String?) : Observable<GetAllRequiredDishDocsResponse?>?

    /*--------------------  LOGIN MODULE ----------------------------*/

    //send otp
    @POST(WebServer.POST_REGISTRATION_PROCESS_LOGIN)
    fun sendOtp(@Query("mobile_number") mobile_number : String?,@Query("api_token")api_token: String?):  Observable<SendOtpResponse?>?

    //verify the otp
    @POST(WebServer.POST_VERITY_OTP)
    fun verifyOtp(@Query("api_token")api_token: String?,@Body jsonObject: JsonObject) : Observable<OtpVerificationResponse>


    /*------------------------------------- RESTAURANT MODULE---------------------------*/

    //add restaurant owner details
    @POST(WebServer.POST_ADD_RESTAURANT_OWNER_DETAILS)
    fun addRestaurantOwnerDetails(@Query("api_token") api_token: String?,@Body jsonObject: JsonObject) : Observable<AddRestaurantOwnerDetailsResponse?>?

    //add restaurant details
    @POST(WebServer.POST_ADD_RESTAURANT_PROFILE_DETAILS)
    fun addRestaurantProfileDetails(@Query("api_token") api_token: String?,@Body jsonObject: JsonObject) : Observable<AddRestaurantProfileResponse?>?

    //add restaurant profile photo
    @Multipart
    @POST(WebServer.POST_ADD_RESTAURANT_PROFILE_PHOTO)
    fun addRestaurantProfilePhoto(@Part image :MultipartBody.Part,@Part("title")title: RequestBody,@Part("request_token")request_token: RequestBody) : Observable<AddRestaurantPhotoResponse?>?

    //to track the registration process
    @GET(WebServer.POST_TRACK_REGISTRATION_PROCESS)
    fun trackRegistrationProcess(@Query("api_token") api_token: String?, @Query("request_token") request_token:String?) : Observable<TrackRegistrationProcessResponse?>?

    //check is account verify or not
    @GET(WebServer.GET_IS_ACCOUNT_VERIFY)
    fun isAccountVerify(@Query("api_token")api_token: String?,@Query("request_token")request_token: String?) : Observable<IsAccountVerifyResponse?>?

    //to add a new dish/menu
    @Multipart
    @POST(WebServer.POST_ADD_NEW_DISH)
    fun addNewDish(@Part image : MultipartBody.Part , @PartMap map: Map<String, @JvmSuppressWildcards RequestBody >) : Observable<AddNewDishResponse?>?

    //get all menus
    @GET(WebServer.GET_ALL_MENUS)
    fun getAllMenus(@Query("api_token")api_token: String?,@Query("restaurant_id") restaurant_id: String,@Query("restaurant_token_id")restaurant_token_id:String) : Observable<GetMenusResponse?>?


    /*----------------------------------------------  FILTER OPTIONS --------------------------------*/

    //get all filter options
    @GET(WebServer.GET_ALL_FILTER_OPTIONS)
    fun getAllFilterOptions(@Query("api_token")api_token: String?) : Observable<GetAllFilterOptionResponse?>?

    @POST(WebServer.POST_GET_DISH_WITH_FILTER)
    fun getAllDishWithFilter(@Query("api_token") api_token: String?,@Body jsonObject: JsonObject) : Observable<GetAllDishWithFilterOptionResponse?>?
}