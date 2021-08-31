package com.voila.voilasailor.restaurantRegistration.RestaurantRespository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.MenuUpdateResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.MenuUpdateWithImageResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateMenuRepository {

    private var menuUpdateLiveData : MutableLiveData<MenuUpdateResponse> = MutableLiveData()
    private var menuUpdateWithImageLiveData : MutableLiveData<MenuUpdateWithImageResponse> = MutableLiveData()

    fun menuUpdateObservable():MutableLiveData<MenuUpdateResponse>{
        return menuUpdateLiveData
    }

    fun menuUpdateWithInageObservable():MutableLiveData<MenuUpdateWithImageResponse>{
        return menuUpdateWithImageLiveData
    }



    @SuppressLint("CheckResult")
    fun updateMenu(jsonObject: JsonObject){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val updateDishObservable : Observable<MenuUpdateResponse?>? = apiClient.updateMenuInfo(jsonObject)

        updateDishObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                menuUpdateLiveData.postValue(it)
            },{
                menuUpdateLiveData.postValue(null)
            })
    }

    @SuppressLint("CheckResult")
    fun menuUpdateWithImage(imgBody: MultipartBody.Part, map: Map<String, @JvmSuppressWildcards RequestBody>){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val updateDishWithImageObservable : Observable<MenuUpdateWithImageResponse?>? = apiClient.updateMenuInfoWithImage(imgBody,map)

        updateDishWithImageObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                menuUpdateWithImageLiveData.postValue(it)
            },{
                menuUpdateWithImageLiveData.postValue(null)
            })

    }


}