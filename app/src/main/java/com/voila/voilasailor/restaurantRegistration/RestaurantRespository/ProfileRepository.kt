package com.voila.voilasailor.restaurantRegistration.RestaurantRespository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.API.WebServer
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantOwnerDetailsResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantPhotoResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantProfileResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.GetRestaurantRequestedInfoResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileRepository {

    var getRestaurantRequestedLiveData : MutableLiveData<GetRestaurantRequestedInfoResponse> = MutableLiveData()
    var addRestaurantProfileDetailsLiveData : MutableLiveData<AddRestaurantProfileResponse> = MutableLiveData()
    var addRestaurantPhotoLiveData : MutableLiveData<AddRestaurantPhotoResponse> = MutableLiveData()
    var addRestaurantOwnerDetailsLiveData : MutableLiveData<AddRestaurantOwnerDetailsResponse> = MutableLiveData()



    /*------------------------------ OBSERVABLE FUN---------------------------*/

    //add restaurant owner observable
    fun addRestaurantObservable() : MutableLiveData<AddRestaurantOwnerDetailsResponse>{
        return addRestaurantOwnerDetailsLiveData
    }

    //get restaurant requested info
    fun getRestaurantRequestedInfObservable() : MutableLiveData<GetRestaurantRequestedInfoResponse>{
        return getRestaurantRequestedLiveData
    }

    //add restaurant profile details observable
    fun addRestaurantProfileDetailsObservable() : MutableLiveData<AddRestaurantProfileResponse>{
        return addRestaurantProfileDetailsLiveData
    }

    //add restaurant photo observable
    fun addRestaurantPhotoObservable() : MutableLiveData<AddRestaurantPhotoResponse>{
        return  addRestaurantPhotoLiveData
    }

    /*----------------------------------  CALLING REPO FUN ------------------------------*/

    //get restaurant requested info
    @SuppressLint("CheckResult")
    fun getRestaurantRequestedInfo(request_token: String){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val getRestaurantRequestedInfoObservable : Observable<GetRestaurantRequestedInfoResponse?>? = apiClient.getRestaurantRequestedInformation(
            WebServer.external_api_token,request_token)

        getRestaurantRequestedInfoObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                getRestaurantRequestedLiveData.postValue(it)
            },{
                getRestaurantRequestedLiveData.postValue(null)
            })
    }

    //add restaurant profile details
    @SuppressLint("CheckResult")
    fun updateRestaurantProfileDetails(jsonObject: JsonObject){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val addRestaurantProfile : Observable<AddRestaurantProfileResponse?>? = apiClient.updateRestaurantDetails(WebServer.external_api_token,jsonObject)

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
    fun updateRestaurantPhoto(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addRestaurantPhotoObservable : Observable<AddRestaurantPhotoResponse?>? = apiClient.updateRestaurantDocument(body,title,request_token)

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

    //add restaurant owner details
    @SuppressLint("CheckResult")
    fun updateRestaurantOwnerDetails(jsonObject: JsonObject){
        // Log.d("repoJson", "addRestaurantOwnerDetails: $jsonObject")
        val apiClient = ApiClient.RetrofitCall.retrofit
        val addRestaurantDetailsObservable : Observable<AddRestaurantOwnerDetailsResponse?>? = apiClient.updateOwnerDetails(WebServer.external_api_token,jsonObject)

        addRestaurantDetailsObservable?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                addRestaurantOwnerDetailsLiveData.postValue(it)
            },{
                addRestaurantOwnerDetailsLiveData.postValue(null)
            })
    }

}