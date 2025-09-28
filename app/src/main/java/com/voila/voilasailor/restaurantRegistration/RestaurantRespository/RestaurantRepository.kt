package com.voila.voilasailor.restaurantRegistration.RestaurantRespository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.API.WebServer
import com.google.gson.JsonObject
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RestaurantRepository {

    var addRestaurantOwnerDetailsLiveData : MutableLiveData<AddRestaurantOwnerDetailsResponse> = MutableLiveData()
    var trackRestaurantRegistrationLiveData : MutableLiveData<TrackRegistrationProcessResponse> = MutableLiveData()
    var addRestaurantProfileDetailsLiveData : MutableLiveData<AddRestaurantProfileResponse> = MutableLiveData()
    var addRestaurantPhotoLiveData : MutableLiveData<AddRestaurantPhotoResponse> = MutableLiveData()
    var getRestaurantVerificationLiveData : MutableLiveData<RestaurantVerificationTrackResponse> = MutableLiveData()

    /*------------------------------ OBSERVABLE FUN---------------------------*/

    //add restaurant owner observable
    fun addRestaurantObservable() : MutableLiveData<AddRestaurantOwnerDetailsResponse>{
        return addRestaurantOwnerDetailsLiveData
    }

    //track registration process observable
    fun trackRegistrationProcessObservable() : MutableLiveData<TrackRegistrationProcessResponse>{
        return  trackRestaurantRegistrationLiveData
    }

    //add restaurant profile details observable
    fun addRestaurantProfileDetailsObservable() : MutableLiveData<AddRestaurantProfileResponse>{
        return addRestaurantProfileDetailsLiveData
    }

    //add restaurant photo observable
    fun addRestaurantPhotoObservable() : MutableLiveData<AddRestaurantPhotoResponse>{
        return  addRestaurantPhotoLiveData
    }


    // get restaurant verification tracker
    fun verificationTrackObservable(): MutableLiveData<RestaurantVerificationTrackResponse>{
        return getRestaurantVerificationLiveData
    }

    /*----------------------------------- CALL FUN -----------------------------*/

    //add restaurant owner details
    @SuppressLint("CheckResult")
    fun addRestaurantOwnerDetails(jsonObject: JsonObject){
       // Log.d("repoJson", "addRestaurantOwnerDetails: $jsonObject")
        val apiClient = ApiClient.RetrofitCall.retrofit
        val addRestaurantDetailsObservable : Observable<AddRestaurantOwnerDetailsResponse?>? = apiClient.addRestaurantOwnerDetails(WebServer.external_api_token,jsonObject)
        addRestaurantDetailsObservable?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(Schedulers.computation())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                            addRestaurantOwnerDetailsLiveData.postValue(it)
                },{
                    addRestaurantOwnerDetailsLiveData.postValue(null)
                })
    }

    //to track registration process
    @SuppressLint("CheckResult")
    fun trackRegistrationProcess(request_token:String){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val trackRegistrationProcess : Observable<TrackRegistrationProcessResponse?>? = apiClient.trackRegistrationProcess(WebServer.external_api_token,request_token)

        trackRegistrationProcess
                ?.subscribeOn(Schedulers.io())
                ?.unsubscribeOn(Schedulers.computation())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    trackRestaurantRegistrationLiveData.postValue(it)
                },{
                    trackRestaurantRegistrationLiveData.postValue(null)

                })
    }

    //add restaurant profile details
    @SuppressLint("CheckResult")
    fun addRestaurantProfileDetails(jsonObject: JsonObject){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val addRestaurantProfile : Observable<AddRestaurantProfileResponse?>? = apiClient.addRestaurantProfileDetails(WebServer.external_api_token,jsonObject)

        addRestaurantProfile
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                addRestaurantProfileDetailsLiveData.postValue(it)
            },{
                addRestaurantOwnerDetailsLiveData.postValue(null)

            })
    }

    //add restaurant photo
    @SuppressLint("CheckResult")
    fun addRestaurantPhoto(body: MultipartBody.Part, title:RequestBody, request_token: RequestBody){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addRestaurantPhotoObservable : Observable<AddRestaurantPhotoResponse?>? = apiClient.addRestaurantProfilePhoto(body,title,request_token)

        addRestaurantPhotoObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                addRestaurantPhotoLiveData.postValue(it)
            },{
                addRestaurantPhotoLiveData.postValue(null)
            })
    }

    // get restaurant verification tracker
    @SuppressLint("CheckResult")
    fun getRestVerificationTrack(tag:String, request_token:String){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val restaurantVerificationObservable : Observable<RestaurantVerificationTrackResponse?>? = apiClient.getRestaurantVerification(WebServer.external_api_token,tag, request_token)

        restaurantVerificationObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                getRestaurantVerificationLiveData.postValue(it)
            },{
                getRestaurantVerificationLiveData.postValue(null)
            })
    }

}