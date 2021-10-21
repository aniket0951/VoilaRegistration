package com.voila.voilasailor.driverRegistration.Repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.API.WebServer
import com.voila.voilasailor.driverRegistration.NetworkResponse.ConformRateCardResponse
import com.voila.voilasailor.driverRegistration.NetworkResponse.CustomRateCardResponse
import com.voila.voilasailor.driverRegistration.NetworkResponse.DriverRequestedInfoResponse
import com.voila.voilasailor.driverRegistration.NetworkResponse.SystemRateCardResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RateCardRepository {

    private val systemRateCardLiveData:MutableLiveData<SystemRateCardResponse> = MutableLiveData()
    private val conformRateCardLiveData : MutableLiveData<ConformRateCardResponse> = MutableLiveData()
    private val customRateCardLiveData: MutableLiveData<CustomRateCardResponse> = MutableLiveData()

    fun getSystemRateCardObservable(): MutableLiveData<SystemRateCardResponse>{
        return systemRateCardLiveData
    }

    fun conformRateCardObservable(): MutableLiveData<ConformRateCardResponse>{
        return conformRateCardLiveData
    }
    fun customRateCardObservable(): MutableLiveData<CustomRateCardResponse>{
        return customRateCardLiveData
    }

    @SuppressLint("CheckResult")
    fun getSystemRateCard(auth_token:String){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val getDriverRequestedObservable : Observable<SystemRateCardResponse?>? = apiClient.getSystemRateCard(auth_token)

        getDriverRequestedObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                systemRateCardLiveData.postValue(it)
            },{
                systemRateCardLiveData.postValue(null)
            })

    }

    @SuppressLint("CheckResult")
    fun conformRateCard(jsonObject: JsonObject){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val conformRateCardObservable : Observable<ConformRateCardResponse?>? = apiClient.conformRateCard(jsonObject)

        conformRateCardObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                conformRateCardLiveData.postValue(it)
            },{
                conformRateCardLiveData.postValue(null)
            })
    }

    @SuppressLint("CheckResult")
    fun customRateCard(jsonObject: JsonObject){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val customRateCardObservable : Observable<CustomRateCardResponse?>? = apiClient.getCustomRateCard(jsonObject)

        customRateCardObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                customRateCardLiveData.postValue(it)
            },{
                customRateCardLiveData.postValue(null)
            })

    }
}