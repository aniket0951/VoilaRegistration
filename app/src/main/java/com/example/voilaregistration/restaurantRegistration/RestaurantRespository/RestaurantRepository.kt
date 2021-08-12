package com.example.voilaregistration.restaurantRegistration.RestaurantRespository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.voilaregistration.API.ApiClient
import com.example.voilaregistration.API.WebServer
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantOwnerDetailsResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.TrackRegistrationProcessResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RestaurantRepository {

    var addRestaurantOwnerDetailsLiveData : MutableLiveData<AddRestaurantOwnerDetailsResponse> = MutableLiveData()
    var trackRestaurantRegistrationLiveData : MutableLiveData<TrackRegistrationProcessResponse> = MutableLiveData()

    /*------------------------------ OBSERVABLE FUN---------------------------*/

    //add restaurant owner observable
    fun addRestaurantObservable() : MutableLiveData<AddRestaurantOwnerDetailsResponse>{
        return addRestaurantOwnerDetailsLiveData
    }

    //track registration process observable
    fun trackRegistrationProcessObservable() : MutableLiveData<TrackRegistrationProcessResponse>{
        return  trackRestaurantRegistrationLiveData
    }

    /*----------------------------------- CALL FUN -----------------------------*/

    //add restaurant owner details
    fun addRestaurantOwnerDetails(jsonObject: JsonObject){
        Log.d("repoJson", "addRestaurantOwnerDetails: $jsonObject")
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
}