package com.example.voilaregistration.Repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.voilaregistration.API.ApiClient
import com.example.voilaregistration.NetworkResponse.GetAllRequiredDocsResponse
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class MianRepository {

     var getAllRequiredDocsResponse: MutableLiveData<GetAllRequiredDocsResponse> = MutableLiveData()
     var getAllRequiredRestaurantDocsResponse : MutableLiveData<GetAllRestaurantDocsResponse> = MutableLiveData()

    /*------------------------------- GENERATE OBSERVABLE -----------------------*/
    fun  getAllRequiredDocsObservable(): MutableLiveData<GetAllRequiredDocsResponse> {
        return getAllRequiredDocsResponse
    }

    fun getAllRequiredRestaurantObservable() : MutableLiveData<GetAllRestaurantDocsResponse>{
        return getAllRequiredRestaurantDocsResponse
    }

    /*--------------------------------  NETWORK CALL FROM REPO ----------------------*/

    @SuppressLint("CheckResult")
    fun getAllRequiredDocs(title: String){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val getAllRequiredDocsResponseObservable: Observable<GetAllRequiredDocsResponse?>? = apiClient.getAllRequiredDocs(title)
        getAllRequiredDocsResponseObservable?.subscribeOn(Schedulers.io())?.unsubscribeOn(Schedulers.computation())?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
            getAllRequiredDocsResponse.postValue(it)
        },{
            getAllRequiredDocsResponse.postValue(null)
        })
    }

    @SuppressLint("CheckResult")
    fun getAllRequiredRestaurantDocs(title: String){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val getAllRequiredDocsResponseObservable: Observable<GetAllRestaurantDocsResponse?>? = apiClient.getAllRequiredRestaurantDocs(title)
        getAllRequiredDocsResponseObservable?.subscribeOn(Schedulers.io())?.unsubscribeOn(Schedulers.computation())?.observeOn(AndroidSchedulers.mainThread())?.subscribe({
            getAllRequiredRestaurantDocsResponse.postValue(it)
        },{
            getAllRequiredRestaurantDocsResponse.postValue(null)
        })
    }
}