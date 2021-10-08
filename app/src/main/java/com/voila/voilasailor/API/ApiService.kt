package com.voila.voilasailor.API

import com.google.gson.JsonObject
import com.voila.voilasailor.NetworkResponse.GetAllRequiredDocsResponse
import com.voila.voilasailor.NetworkResponse.GetAllRestaurantDocsResponse
import com.voila.voilasailor.driverRegistration.NetworkResponse.*
import com.voila.voilasailor.loginModule.NetworkResponse.OtpVerificationResponse
import com.voila.voilasailor.loginModule.NetworkResponse.SendOtpResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.*
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

    //update menu
    @Multipart
    @POST(WebServer.POST_UPDATE_MENU)
    fun updateMenuInfoWithImage(@Part image : MultipartBody.Part , @PartMap map: Map<String, @JvmSuppressWildcards RequestBody >) : Observable<MenuUpdateWithImageResponse?>?


    @POST(WebServer.POST_UPDATE_MENU)
    fun updateMenuInfo(@Body jsonObject: JsonObject) : Observable<MenuUpdateResponse?>?

    //remove the dish
    @POST(WebServer.POST_REMOVE_DISH)
    fun removeDish(@Query("api_token")api_token: String?,@Body jsonObject: JsonObject) : Observable<DishRemoveResponse?>?

    //get restaurant requested information
    @GET(WebServer.GET_RESTAURANT_REQUESTED_INFO)
    fun getRestaurantRequestedInformation(@Query("api_token")api_token: String?,@Query("request_token")request_token:String) : Observable<GetRestaurantRequestedInfoResponse?>?

    //update owner details
    @POST(WebServer.POST_UPDATE_OWNER_DETAILS)
    fun updateOwnerDetails(@Query("api_token") api_token: String?,@Body jsonObject: JsonObject) : Observable<AddRestaurantOwnerDetailsResponse?>?

    //update restaurant details
    @POST(WebServer.POST_UPDATE_RESTAURANT_DETAILS)
    fun updateRestaurantDetails(@Query("api_token") api_token: String?,@Body jsonObject: JsonObject) : Observable<AddRestaurantProfileResponse?>?

    //update restaurant documents
    @Multipart
    @POST(WebServer.POST_UPDATE_RESTAURANT_DOCUMENT)
    fun updateRestaurantDocument(@Part image :MultipartBody.Part,@Part("title")title: RequestBody,@Part("request_token")request_token: RequestBody) : Observable<AddRestaurantPhotoResponse?>?





    /*----------------------------------------------  DRIVER MODULE --------------------------------*/
    @GET(WebServer.GET_TRACK_DRIVER_REGISTRATION)
    fun trackDriverRegistrationProcess(@Query("api_token")api_token: String?,@Query("request_token")request_token: String?) : Observable<TrackDriverRegistrationProccessResponse?>?

    //add basic information
    @POST(WebServer.POST_ADD_PERSONAL_INFORMATION)
    fun addPersonalInformation(@Query("api_token")api_token: String?,@Body jsonObject: JsonObject) : Observable<AddPersonalInformationResponse?>?

    //add address details
    @POST(WebServer.POST_ADD_ADDRESS_DETAILS)
    fun addAddressDetails(@Query("api_token")api_token: String?,@Body jsonObject: JsonObject) : Observable<AddAddressResponse?>?

    //add kyc documents

    @Multipart
    @POST(WebServer.POST_ADD_KYC_DOCUMENTS)
    fun addKYCDocs(@Part image :MultipartBody.Part,@Part("title")title: RequestBody,@Part("request_token")request_token: RequestBody) : Observable<AddKYCDocumentResponse?>?

    //add vehicle details
    @POST(WebServer.POST_ADD_VEHICLE_DETAILS)
    fun addVehicleDetails(@Query("api_token")api_token: String?,@Body jsonObject: JsonObject) : Observable<AddVehicleDetailsResponse?>?

    @Multipart
    @POST(WebServer.POST_ADD_VEHICLE_PROFILE)
    fun addVehicleProfile(@Part image :MultipartBody.Part,@Part("title")title: RequestBody,@Part("request_token")request_token: RequestBody) : Observable<AddVehicleProfileResponse?>?

    //get all requested information before account is verify
    @GET(WebServer.GET_ALL_REQUESTED_INFORMATION)
    fun getAllRequestedInformation(@Query("api_token")api_token: String?,@Query("request_token")request_token: String) : Observable<DriverRequestedInfoResponse>?

    //update personal info
    @POST(WebServer.POST_UPDATE_PERSONAL_INFORMATION)
    fun updatePersonalInformation(@Query("api_token")api_token: String?,@Body jsonObject: JsonObject) : Observable<AddPersonalInformationResponse?>?

    //update vehicle info
    @POST(WebServer.POST_UPDATE_VEHICLE_INFORMATION)
    fun updateVehicleInformation(@Query("api_token")api_token: String?,@Body jsonObject: JsonObject) : Observable<AddVehicleDetailsResponse?>?

    //update address info
    @POST(WebServer.POST_UPDATE_ADDRESS_INFORMATION)
    fun updateAddressInformation(@Query("api_token")api_token: String?,@Body jsonObject: JsonObject) : Observable<AddAddressResponse?>?

    @Multipart
    @POST(WebServer.POST_UPDATE_KYC_INFORMATION)
    fun updateKYCInformation(@Part image :MultipartBody.Part,@Part("title")title: RequestBody,@Part("request_token")request_token: RequestBody) : Observable<AddKYCDocumentResponse?>?

    @Multipart
    @POST(WebServer.POST_UPDATE_VEHICLE_DOCUMENTS)
    fun updateVehicleDocuments(@Part image :MultipartBody.Part,@Part("title")title: RequestBody,@Part("request_token")request_token: RequestBody) : Observable<AddVehicleProfileResponse?>?

    //get system rate card
    @GET(WebServer.GET_SYSTEM_RATE_CARD)
    fun getSystemRateCard(@Query("auth_token")auth_token:String): Observable<SystemRateCardResponse?>?

    @POST(WebServer.POST_CONFORM_RATE_CARD)
    fun conformRateCard(@Body jsonObject: JsonObject): Observable<ConformRateCardResponse?>?

    @POST(WebServer.POST_CUSTOM_RATE_CARD)
    fun getCustomRateCard(@Body jsonObject: JsonObject): Observable<CustomRateCardResponse?>?

    /*----------------------------------------------  FILTER OPTIONS --------------------------------*/

    //get all filter options
    @GET(WebServer.GET_ALL_FILTER_OPTIONS)
    fun getAllFilterOptions(@Query("api_token")api_token: String?) : Observable<GetAllFilterOptionResponse?>?

    @POST(WebServer.POST_GET_DISH_WITH_FILTER)
    fun getAllDishWithFilter(@Query("api_token") api_token: String?,@Body jsonObject: JsonObject) : Observable<GetAllDishWithFilterOptionResponse?>?
}