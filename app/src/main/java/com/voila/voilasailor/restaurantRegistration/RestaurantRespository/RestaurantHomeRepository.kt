package com.voila.voilasailor.restaurantRegistration.RestaurantRespository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.API.WebServer
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.*
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RestaurantHomeRepository {

    //to check is account verify or not
    private val isAccountVerifyLiveData : MutableLiveData<IsAccountVerifyResponse> = MutableLiveData()
    private val getMenusLiveData : MutableLiveData<GetMenusResponse> = MutableLiveData()
    private val getAllFilterOptionsLiveData : MutableLiveData<GetAllFilterOptionResponse> = MutableLiveData()
    private val getAllDishWithFilterOptionLiveData : MutableLiveData<GetAllDishWithFilterOptionResponse> = MutableLiveData()
    var dishRemoveLiveData : MutableLiveData<DishRemoveResponse> = MutableLiveData()


    /*--------------------------------  CALLING OBSERVABLE -------------------------*/

    //to check is account verify or not
    fun isAccountVerifyObservable() : MutableLiveData<IsAccountVerifyResponse>{
        return isAccountVerifyLiveData
    }

    //get all menus list observable
    fun getMenusObservable() : MutableLiveData<GetMenusResponse>{
        return getMenusLiveData
    }

    //get all filter options
    fun getAllFilterOptionsObservable() : MutableLiveData<GetAllFilterOptionResponse>{
        return getAllFilterOptionsLiveData
    }

    //get all dish with filter options
    fun getAllDishWithFilterOptionObservable() : MutableLiveData<GetAllDishWithFilterOptionResponse>{
        return getAllDishWithFilterOptionLiveData
    }

    //remove dish
    fun dishRemoveObservable() : MutableLiveData<DishRemoveResponse>{
        return dishRemoveLiveData
    }



    /*----------------------------------  CALLING REPO FUN ------------------------------*/

    // to check is account verify
    @SuppressLint("CheckResult")
    fun isAccountVerify(request_token: String){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val accountVerifyObservable : Observable<IsAccountVerifyResponse?>? = apiClient.isAccountVerify(
            WebServer.external_api_token,request_token)

        accountVerifyObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                isAccountVerifyLiveData.postValue(it)
            },{
                isAccountVerifyLiveData.postValue(null)
            })
    }

    //get all menus
    @SuppressLint("CheckResult")
    fun getAllMenus(restaurant_token_id:String, restaurant_id:String){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val getAllMenuObservable : Observable<GetMenusResponse?>? = apiClient.getAllMenus(WebServer.external_api_token,restaurant_id,restaurant_token_id)

        getAllMenuObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                getMenusLiveData.postValue(it)
            },{
                getMenusLiveData.postValue(null)
            })
    }

    //get all filter option
    @SuppressLint("CheckResult")
    fun getAllFilterOptions(){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val getFilterOptionObservable : Observable<GetAllFilterOptionResponse?>? = apiClient.getAllFilterOptions(WebServer.external_api_token)

        getFilterOptionObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                 getAllFilterOptionsLiveData.postValue(it)
            },{
                getAllFilterOptionsLiveData.postValue(null)
            })
    }

    //get All dish with filter options
    @SuppressLint("CheckResult")
    fun getAllDishWithFilterOption(jsonObject: JsonObject){
        val  apiClient = ApiClient.RetrofitCall.retrofit
        val getAllDishWithFilterOptionObservable : Observable<GetAllDishWithFilterOptionResponse?>? = apiClient.getAllDishWithFilter(WebServer.external_api_token,jsonObject)

        getAllDishWithFilterOptionObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                getAllDishWithFilterOptionLiveData.postValue(it)
            },{
                getAllDishWithFilterOptionLiveData.postValue(null)
            })
    }

    //remove the dish
    @SuppressLint("CheckResult")
    fun dishRemove(jsonObject: JsonObject){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val dishRemoveObservable  : Observable<DishRemoveResponse?>? = apiClient.removeDish(WebServer.external_api_token,jsonObject)

        dishRemoveObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                dishRemoveLiveData.postValue(it)
            },{
                dishRemoveLiveData.postValue(null)
            })
    }


}