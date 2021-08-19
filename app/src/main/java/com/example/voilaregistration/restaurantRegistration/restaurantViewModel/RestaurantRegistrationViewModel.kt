package com.example.voilaregistration.restaurantRegistration.restaurantViewModel

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import com.example.voilaregistration.Repository.MianRepository
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantOwnerDetailsResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantPhotoResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.AddRestaurantProfileResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.TrackRegistrationProcessResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantRespository.RestaurantRepository
import com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner.RestaurantViewModelListener
import com.example.voilaregistration.restaurantRegistration.UI.RestaurantHomeScreenActivity
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RestaurantRegistrationViewModel(var context: Context): ViewModel() {

    lateinit var listener : RestaurantViewModelListener
    lateinit var progressDialog: ProgressDialog
     val processCompleteStatusCode = ObservableField<String>()

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

    //add restaurant owner details
    fun _addRestaurantOwnerDetails(jsonObject: JsonObject){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait..........")
        addRestaurantOwnerDetails(jsonObject)
        listener.onAddRestaurantOwnerDetailsSuccess()
    }

    //add restaurant profile details
    fun _addRestaurantProfileDetails(jsonObject: JsonObject){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait.....adding your restaurant profile details")
        addRestaurantProfileDetails(jsonObject)
        listener.onAddRestaurantProfileDetailsSuccess()
    }

    fun trackRegistrationProcess(){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait getting form for you")
        Helper.getAuthToken.authToken(context)?.let { trackRegistrationForm(it) }
        listener.toTrackRegistrationProcessSuccess()
       // Log.d("authToken", "trackRegistrationProcess: " + Helper.getAuthToken.authToken(context))
    }

    //check process status code and do operation
    fun checkProcessCompleteStatus(jsonObject: JsonObject){
        val code:String = processCompleteStatusCode.get().toString()
        when (code) {
            "0"->{
                _addRestaurantOwnerDetails(jsonObject)
                Log.d("conditionCheck", "checkProcessCompleteStatus: " + processCompleteStatusCode.get() + " in first condition")
            }
             "1" -> {
                 _addRestaurantProfileDetails(jsonObject)
                 Log.d("conditionCheck", "checkProcessCompleteStatus: " + processCompleteStatusCode.get() +  "from second condition")
            }
            "2" -> {
                println("Enter a number: ")
               // num = readLine().toString()

               // println("Your number is: " + num + "\n")
            }
            "3" -> {
                println("\nClosing program...")
            }
            else -> {
                println("\nInvalid option!\n")
            }
        }
    }


    //add restaurant profile photo
    fun _addRestaurantPhoto(body: MultipartBody.Part,title: RequestBody,request_token: RequestBody){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Uploading document Please wait..")
        addRestaurantPhoto(title, request_token, body)
        listener.onAddRestaurantPhotoSuccess()
    }


    //move on Home Screen
    fun moveOnRestaurantHome(){
        val intent = Intent(context,RestaurantHomeScreenActivity::class.java)
        context.startActivity(intent)
    }



    /*------------------------------------------ REPO ----------------------------*/
    private val getAllRestaurantDocsResponse : MutableLiveData<GetAllRestaurantDocsResponse> = mainRepository.getAllRequiredRestaurantObservable()
    private val addRestaurantOwnerDetailsLiveData : MutableLiveData<AddRestaurantOwnerDetailsResponse> = restaurantRepository.addRestaurantObservable()
    private val trackRestaurantRegistration : MutableLiveData<TrackRegistrationProcessResponse> = restaurantRepository.trackRegistrationProcessObservable()
    private val addRestaurantProfileDetailsLiveData : MutableLiveData<AddRestaurantProfileResponse> = restaurantRepository.addRestaurantProfileDetailsObservable()
    private val addRestaurantPhotoLiveData : MutableLiveData<AddRestaurantPhotoResponse> = restaurantRepository.addRestaurantPhotoObservable()

    /*----------------------------------------- CALLING REPO FUN -----------------------*/
    private fun getAllRequiredRestaurantDocs(title: String){
        mainRepository.getAllRequiredRestaurantDocs(title)
    }

    //add restaurant owner details
    private fun addRestaurantOwnerDetails(jsonObject: JsonObject){
        restaurantRepository.addRestaurantOwnerDetails(jsonObject)
    }

    //track the registration form
    private fun trackRegistrationForm(request_token:String){
        restaurantRepository.trackRegistrationProcess(request_token)
    }

    //add restaurant profile details
    private fun addRestaurantProfileDetails(jsonObject: JsonObject){
        restaurantRepository.addRestaurantProfileDetails(jsonObject)
    }

    private fun addRestaurantPhoto(title: RequestBody,request_token: RequestBody,body: MultipartBody.Part){
        restaurantRepository.addRestaurantPhoto(body,title,request_token)
    }


    /*-------------------------------------- OBSERVABLE --------------------------*/
    fun getAllRequiredRestaurantObservable() : MutableLiveData<GetAllRestaurantDocsResponse>{
        return getAllRestaurantDocsResponse
    }

    //add restaurant owner details observable
    fun getRestaurantOwnerDetailsObservable() : MutableLiveData<AddRestaurantOwnerDetailsResponse>{
        return addRestaurantOwnerDetailsLiveData;
    }

    //track registration from observable
    fun trackRegistrationFormObservable() : MutableLiveData<TrackRegistrationProcessResponse>{
        return trackRestaurantRegistration
    }

    //add restaurant profile details
    fun addRestaurantProfileDetailsObservable() : MutableLiveData<AddRestaurantProfileResponse>{
        return addRestaurantProfileDetailsLiveData
    }

    //add restaurant photo observable
    fun addRestaurantPhotoObservable() : MutableLiveData<AddRestaurantPhotoResponse>{
        return addRestaurantPhotoLiveData
    }
}