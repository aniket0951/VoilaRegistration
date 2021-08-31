package com.voila.voilasailor.restaurantRegistration.restaurantViewModel

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.Helper.NetworkStatus
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.MenuUpdateResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.MenuUpdateWithImageResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantRespository.UpdateMenuRepository
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.UpdateMenuViewModelListener
import com.voila.voilasailor.restaurantRegistration.UI.RestaurantHomeScreenActivity
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateMenuViewModel(var context: Context) : ViewModel() {

    val dishName = ObservableField<String>()
    val dishType = ObservableField<String>()
    val dishDescription = ObservableField<String>()
    val menuUrl = ObservableField<String>()
    val dishPrice = ObservableField<String>()
    val dishItem = ObservableField<String>()

    var updateMenuRepository : UpdateMenuRepository = UpdateMenuRepository()

    lateinit var progressDai : ProgressDialog
    lateinit var listener : UpdateMenuViewModelListener

    fun updateMenuItem(){
        Log.d("dishMenu", "updateMenuItem: "+ dishName.get() + dishType.get() + dishPrice.get() + dishItem.get())
    }

    fun showDialog(){
        progressDai = Helper.DialogsUtils.showProgressDialog(context,"Updating dish info")
    }
    fun dismissDialog(){
        if (progressDai!=null) progressDai.dismiss()
    }

    fun moveOnHomeScreen(){
        val intent = Intent(context,RestaurantHomeScreenActivity::class.java)
        context.startActivity(intent)
    }

    fun _updateMenuWithImage(imgBody: MultipartBody.Part, map: Map<String, @JvmSuppressWildcards RequestBody>){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDai = Helper.DialogsUtils.showProgressDialog(
                context,
                "Please wait dish information updating.."
            )
            updateMenuWithImage(imgBody, map)
            listener.onMenuUpdateWithImage()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    fun _updateMenu(jsonObject: JsonObject){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDai = Helper.DialogsUtils.showProgressDialog(
                context,
                "Please wait dish information updating.."
            )
            updateMenu(jsonObject)
            listener.onMenuUpdate()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }


    private val updateMenuLiveData : MutableLiveData<MenuUpdateResponse> = updateMenuRepository.menuUpdateObservable()
    private val updateMenuWithImageLiveData : MutableLiveData<MenuUpdateWithImageResponse> = updateMenuRepository.menuUpdateWithInageObservable()


    fun updateMenu( jsonObject: JsonObject){
        updateMenuRepository.updateMenu(jsonObject)
    }

    fun updateMenuWithImage(imgBody: MultipartBody.Part, map: Map<String, @JvmSuppressWildcards RequestBody>){

        updateMenuRepository.menuUpdateWithImage(imgBody, map)
    }




    fun updateMenuObservable() : MutableLiveData<MenuUpdateResponse>{
        return updateMenuLiveData
    }

    fun updateMenuWithImageObservable(): MutableLiveData<MenuUpdateWithImageResponse>{
        return updateMenuWithImageLiveData
    }


}