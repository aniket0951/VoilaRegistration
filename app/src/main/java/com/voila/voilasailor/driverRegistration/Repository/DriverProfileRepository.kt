package com.voila.voilasailor.driverRegistration.Repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.API.WebServer
import com.voila.voilasailor.driverRegistration.NetworkResponse.DriverRequestedInfoResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DriverProfileRepository() {

    private val getRequestedLiveData : MutableLiveData<DriverRequestedInfoResponse> = MutableLiveData()



    /*-----------------------------  Observable -------------------------------*/
    fun getRequestedObservable() : MutableLiveData<DriverRequestedInfoResponse>{
        return getRequestedLiveData
    }

    /*-----------------------------  Calling Fun -------------------------------*/

    @SuppressLint("CheckResult")
    fun getDriverRequestedInformation(requested_token:String){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val getDriverRequestedObservable : Observable<DriverRequestedInfoResponse>? = apiClient.getAllRequestedInformation(WebServer.external_api_token,requested_token)

        getDriverRequestedObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                getRequestedLiveData.postValue(it)
            },{
                getRequestedLiveData.postValue(null)
            })
    }
}