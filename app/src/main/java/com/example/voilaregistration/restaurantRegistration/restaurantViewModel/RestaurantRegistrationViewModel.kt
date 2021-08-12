package com.example.voilaregistration.restaurantRegistration.restaurantViewModel

import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import com.example.voilaregistration.Repository.MianRepository
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantOwnerDetailsResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantRespository.RestaurantRepository
import com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner.RestaurantViewModelListener
import com.google.gson.JsonObject

class RestaurantRegistrationViewModel(var context: Context): ViewModel() {

    lateinit var listener : RestaurantViewModelListener
    lateinit var progressDialog: ProgressDialog

    var mainRepository: MianRepository = MianRepository()
    var restaurantRepository : RestaurantRepository = RestaurantRepository()

    fun showRegistrationForm(){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait getting form for you")
        getAllRequiredRestaurantDocs("Restaurant")
        listener.onOwnerBasicDetailFound()
    }

    fun dismissProgressDai(){
        if (progressDialog!= null) progressDialog.dismiss()
    }

    fun _addRestaurantOwnerDetails(jsonObject: JsonObject){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait..........")
        addRestaurantOwnerDetails(jsonObject)
        listener.onAddRestaurantOwnerDetailsSuccess()
    }


    /*------------------------------------------ REPO ----------------------------*/
    private val getAllRestaurantDocsResponse : MutableLiveData<GetAllRestaurantDocsResponse> = mainRepository.getAllRequiredRestaurantObservable()
    private val addRestaurantOwnerDetailsLiveData : MutableLiveData<AddRestaurantOwnerDetailsResponse> = restaurantRepository.addRestaurantObservable()
    //+999999999999999999999999private val trackRestaurantRegistration


    /*----------------------------------------- CALLING REPO FUN -----------------------*/
    private fun getAllRequiredRestaurantDocs(title: String){
        mainRepository.getAllRequiredRestaurantDocs(title)
    }

    //add restaurant owner details
    private fun addRestaurantOwnerDetails(jsonObject: JsonObject){
        restaurantRepository.addRestaurantOwnerDetails(jsonObject)
    }

    /*-------------------------------------- OBSERVABLE --------------------------*/
    fun getAllRequiredRestaurantObservable() : MutableLiveData<GetAllRestaurantDocsResponse>{
        return getAllRestaurantDocsResponse
    }

    //add restaurant owner details observable
    fun getRestaurantOwnerDetailsObservable() : MutableLiveData<AddRestaurantOwnerDetailsResponse>{
        return addRestaurantOwnerDetailsLiveData;
    }
}