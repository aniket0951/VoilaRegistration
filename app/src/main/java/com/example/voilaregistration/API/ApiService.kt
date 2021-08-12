package com.example.voilaregistration.API

import com.example.voilaregistration.NetworkResponse.GetAllRequiredDocsResponse
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import com.example.voilaregistration.loginModule.NetworkResponse.OtpVerificationResponse
import com.example.voilaregistration.loginModule.NetworkResponse.SendOtpResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantOwnerDetailsResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.TrackRegistrationProcessResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    /*--------------------  TO GET ALL REQUIRED DOCS ----------------------------*/
    @POST(WebServer.POST_GET_ALL_REQUIRED_DOCS)
    fun getAllRequiredDocs(@Query("title") title: String?): Observable<GetAllRequiredDocsResponse?>?

    @POST(WebServer.POST_GET_ALL_REQUIRED_DOCS)
    fun getAllRequiredRestaurantDocs(@Query("title") title: String?): Observable<GetAllRestaurantDocsResponse?>?


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

    //to track the registration process
    @POST(WebServer.POST_TRACK_REGISTRATION_PROCESS)
    fun trackRegistrationProcess(@Query("api_token") api_token: String?, @Query("request_token") request_token:String?) : Observable<TrackRegistrationProcessResponse?>?
}