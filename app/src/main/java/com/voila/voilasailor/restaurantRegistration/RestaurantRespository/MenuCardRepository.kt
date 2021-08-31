package com.voila.voilasailor.restaurantRegistration.RestaurantRespository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.API.WebServer
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.AddNewDishResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.GetAllRequiredDishDocsResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MenuCardRepository {

    private val addNewDishLiveData : MutableLiveData<AddNewDishResponse> = MutableLiveData()
    private val getAllRequiredDishDocsLiveData : MutableLiveData<GetAllRequiredDishDocsResponse> = MutableLiveData()


    /*------------------------- CREATING OBSERVABLE --------------------------- */

    //add new dish observable
    fun addNewDishObservable() : MutableLiveData<AddNewDishResponse> {
        return addNewDishLiveData
    }

    //get All dish required docs
    fun getAllDishRequiredDocsObservable() : MutableLiveData<GetAllRequiredDishDocsResponse>{
        return getAllRequiredDishDocsLiveData
    }

    /*-------------------------  Network Call ---------------------------- */

    //add new dish
    @SuppressLint("CheckResult")
    fun addNewDish(imgBody: MultipartBody.Part,map: Map<String, @JvmSuppressWildcards RequestBody>){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val addNewDishObservable : Observable<AddNewDishResponse?>? = apiClient.addNewDish(imgBody,map)

        addNewDishObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                  addNewDishLiveData.postValue(it)
            },{
                addNewDishLiveData.postValue(null)
            })
    }

    //get All Required dish docs
    @SuppressLint("CheckResult")
    fun getAllRequiredDishDocs(){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val getAllRequiredDishDocsObservable : Observable<GetAllRequiredDishDocsResponse?>? = apiClient.getAllRequiredDishDocs(WebServer.external_api_token)

        getAllRequiredDishDocsObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                getAllRequiredDishDocsLiveData.postValue(it)
            },{
                getAllRequiredDishDocsLiveData.postValue(null)
            })
    }
}