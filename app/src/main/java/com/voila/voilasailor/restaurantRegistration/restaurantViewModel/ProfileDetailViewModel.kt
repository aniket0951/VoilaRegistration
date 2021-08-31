package com.voila.voilasailor.restaurantRegistration.restaurantViewModel

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.Helper.NetworkStatus
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantOwnerDetailsResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantPhotoResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantProfileResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.GetRestaurantRequestedInfoResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantRespository.ProfileRepository
import com.voila.voilasailor.restaurantRegistration.RestaurantRespository.RestaurantRepository
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.ProfileListener
import com.voila.voilasailor.restaurantRegistration.UI.RestaurantHomeScreenActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileDetailViewModel(var context: Context) : ViewModel() {

    var profileRepository : ProfileRepository = ProfileRepository()
    private var restaurantRepository : RestaurantRepository = RestaurantRepository()
     lateinit var listener : ProfileListener
     lateinit var progressDai :ProgressDialog

    var isOwnerDetailsShow  : Boolean = false
    var isRestaurantDetailsShow : Boolean = false
    var isRestaurantDocsShow : Boolean = false


     //dismiss the progress dai
     fun dismissDai(){
         if (progressDai!= null) progressDai.dismiss()
     }

    //show owner details
    fun showOwnerDetails(){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDai = Helper.DialogsUtils.showProgressDialog(
                context,
                "Please wait we are getting owner details"
            )
            getRestaurantRequestedInfo(Helper.getAuthToken.authToken(context))
            listener.onOwnerDetailsSuccess()
            isOwnerDetailsShow = true
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    //show restaurant details
    fun showRestaurantDetails(){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDai = Helper.DialogsUtils.showProgressDialog(
                context,
                "Please wait we are getting restaurant details"
            )
            getRestaurantRequestedInfo(Helper.getAuthToken.authToken(context))
            listener.onRestaurantDetailsSuccess()
            isRestaurantDetailsShow = true
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    //show restaurant docs details
    fun showRestaurantDocsDetails(){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDai = Helper.DialogsUtils.showProgressDialog(
                context,
                "Please wait we are getting restaurant document details"
            )
            getRestaurantRequestedInfo(Helper.getAuthToken.authToken(context))
            listener.onRestaurantDocsDetailsSuccess()
            isRestaurantDocsShow = true
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    //cancel the update operation
    fun onCancelClick(){

        val intent = Intent(context,RestaurantHomeScreenActivity::class.java)
        context.startActivity(intent)
    }

    //update the information
    fun updateTheInformation(jsonObject: JsonObject){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            when {
                isOwnerDetailsShow -> {
                    updateTheOwnerDetails(jsonObject)
                }
                isRestaurantDetailsShow -> {
                    updateRestaurantDetails(jsonObject)
                }
                isRestaurantDocsShow -> {
                    onCancelClick()
                }
            }
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    fun updateRestaurantDocsImage(body: MultipartBody.Part,title: RequestBody,request_token: RequestBody){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            addRestaurantPhoto(title, request_token, body)
            listener.onRestaurantDocumentUpdateSuccessfully()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    private fun updateRestaurantDetails(jsonObject: JsonObject) {
       if (NetworkStatus.getInstance(context)?.isOnline()!!) {
           progressDai = Helper.DialogsUtils.showProgressDialog(
               context,
               "Please wait we are updating restaurant details.."
           )
           addRestaurantProfileDetails(jsonObject)
           listener.onRestaurantDetailsUpdateSuccessfully()
       }
       else{
           Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
       }
    }

    private fun updateTheOwnerDetails(jsonObject: JsonObject) {
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDai = Helper.DialogsUtils.showProgressDialog(
                context,
                "Please wait we are updating owner details.."
            )
            addRestaurantOwnerDetails(jsonObject)
            listener.onOwnerDetailsUpdateSuccessfully()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    /*-------------------------------------- IN REPOSITORY -------------------------*/
    var getRestaurantRequestedLiveData : MutableLiveData<GetRestaurantRequestedInfoResponse> = profileRepository.getRestaurantRequestedInfObservable()
    private val addRestaurantOwnerDetailsLiveData : MutableLiveData<AddRestaurantOwnerDetailsResponse> = profileRepository.addRestaurantObservable()
    private val addRestaurantProfileDetailsLiveData : MutableLiveData<AddRestaurantProfileResponse> = profileRepository.addRestaurantProfileDetailsObservable()
    private val addRestaurantPhotoLiveData : MutableLiveData<AddRestaurantPhotoResponse> = profileRepository.addRestaurantPhotoObservable()


    /*------------------------------------ CALL FROM REPO-----------------*/
    private fun getRestaurantRequestedInfo(request_token: String){
        profileRepository.getRestaurantRequestedInfo(request_token)
    }

    //add restaurant owner details
    private fun addRestaurantOwnerDetails(jsonObject: JsonObject){
        profileRepository.updateRestaurantOwnerDetails(jsonObject)
    }

    //add restaurant profile details
    private fun addRestaurantProfileDetails(jsonObject: JsonObject){
        profileRepository.updateRestaurantProfileDetails(jsonObject)
    }

    private fun addRestaurantPhoto(title: RequestBody, request_token: RequestBody, body: MultipartBody.Part){
        profileRepository.updateRestaurantPhoto(body,title,request_token)
    }

    /*-----------------------------------  CALL OBSERVABLE -----------------------*/

    fun getRestaurantRequestedInfoObservable() : MutableLiveData<GetRestaurantRequestedInfoResponse>{
        return getRestaurantRequestedLiveData
    }

    //add restaurant owner details observable
    fun getRestaurantOwnerDetailsObservable() : MutableLiveData<AddRestaurantOwnerDetailsResponse>{
        return addRestaurantOwnerDetailsLiveData;
    }

    //add restaurant profile details
    fun addRestaurantProfileDetailsObservable() : MutableLiveData<AddRestaurantProfileResponse>{
        return addRestaurantProfileDetailsLiveData
    }

    //add restaurant photo observable
    fun addRestaurantPhotoObservable() : MutableLiveData<AddRestaurantPhotoResponse>{
        return addRestaurantPhotoLiveData
    }
}