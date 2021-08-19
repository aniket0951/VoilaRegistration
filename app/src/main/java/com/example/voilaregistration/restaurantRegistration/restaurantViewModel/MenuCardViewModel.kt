package com.example.voilaregistration.restaurantRegistration.restaurantViewModel

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.AddNewDishResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.GetAllRequiredDishDocsResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantRespository.MenuCardRepository
import com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner.MenuCardViewListener
import com.example.voilaregistration.restaurantRegistration.UI.RestaurantHomeScreenActivity
import com.example.voilaregistration.restaurantRegistration.Util.toast
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MenuCardViewModel(var context: Context) : ViewModel() {

    private val menuCardRepository : MenuCardRepository = MenuCardRepository()
    lateinit var listener : MenuCardViewListener
    private lateinit var progressDialog: ProgressDialog


    fun _getAllRequiredDishDocs(){
        getAllRequiredDishDocs()
        listener.onGetRequiredDishDocsSuccess()
    }

    fun _addNewDish(imgBody: MultipartBody.Part,map: Map<String, @JvmSuppressWildcards RequestBody>){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait your menu is adding..")
        addNewDish(imgBody,map)
        listener.onDishAddedSuccessfully()
    }

    //on back arrow click
    fun onBackClick(){
        val intent = Intent(context,RestaurantHomeScreenActivity::class.java)
        context.startActivity(intent)
    }

    //dismiss progress dai
    fun dismissProgressDai(){
        if (progressDialog!= null) progressDialog.dismiss()
    }

    //move on back
    private fun moveOnHomeScreen(){
        listener.onMoveHomeScreen()
    }

    /* check a new menu added successfully or not */
    fun menuAddedSuccessfully(addNewDishResponse: AddNewDishResponse) {
        if (addNewDishResponse.result){
            dismissProgressDai()
            context.toast(addNewDishResponse.message)
            moveOnHomeScreen()
        }
        else if (!addNewDishResponse.result){
            dismissProgressDai()
            context.toast(addNewDishResponse.message)
            moveOnHomeScreen()
        }
    }

    /*----------------------------------------------------------------------------------*/
    private val addNewMenuCardLiveData : MutableLiveData<AddNewDishResponse> = menuCardRepository.addNewDishObservable()
    private val getAllRequiredDishLiveData : MutableLiveData<GetAllRequiredDishDocsResponse> = menuCardRepository.getAllDishRequiredDocsObservable()

    /*------------------------------------ Calling repos fun ----------------------------------*/

    //add new dish
    fun addNewDish(imgBody: MultipartBody.Part,map: Map<String, @JvmSuppressWildcards RequestBody>){
        menuCardRepository.addNewDish(imgBody,map)
    }

    private fun getAllRequiredDishDocs(){
        menuCardRepository.getAllRequiredDishDocs()
    }

    /*-------------------------------------  Calling Observable -----------------------------*/

    //add new dish observable
    fun addNewDishObservable() : MutableLiveData<AddNewDishResponse>{
        return  addNewMenuCardLiveData
    }

    //get All required dish docs observable
    fun getAllRequiredDishObservable() : MutableLiveData<GetAllRequiredDishDocsResponse>{
        return getAllRequiredDishLiveData;
    }
}