package com.voila.voilasailor.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voila.voilasailor.Helper.NetworkChangeReceiver
import com.voila.voilasailor.MainViewModelListener.MainViewModelListener
import com.voila.voilasailor.NetworkResponse.GetAllRequiredDocsResponse
import com.voila.voilasailor.NetworkResponse.GetAllRestaurantDocsResponse
import com.voila.voilasailor.Repository.MianRepository
import com.voila.voilasailor.driverRegistration.DriverRegistrationActivity
import com.voila.voilasailor.restaurantRegistration.RestaurantRegistrationActivity


class MainActivityViewModel(var context: Context) : ViewModel(){


    lateinit var listener: MainViewModelListener
    var mainRepository: MianRepository = MianRepository()
    private val userId = ObservableField<String>()
    private val authToken = ObservableField<String>()
     val registrationFor = ObservableField<String>()
    private val registrationForCheck = ObservableField<String>()

    var isLoading : Boolean = false

    fun isNetworkStateCheck(){
        val intent = Intent(context,NetworkChangeReceiver::class.java)
        context.startService(intent)
    }

    fun registerAsDriver(){
        listener.onDriverRequiredDocs()
        registrationFor.set("Driver")
        getAllRequiredDocs("Driver")
        Log.d("clickCheck", "registerAsDriver: register as driver click")
    }

    fun registerAsRestaurant(){
        listener.onRestaurantRequiredDocs()
        registrationFor.set("Restaurant")
        getAllRequiredRestaurantDocs("Restaurant")
      //  Log.d("clickCheck", "registerAsRestaurant: register restaurant click")
    }

    fun registerAsDeliverPartner(){
        listener.onDriverRequiredDocs()
        registrationFor.set("DeliverPartner")
        getAllRequiredDocs("Driver")
    }

    fun checkUserLogin(){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("voila", Context.MODE_PRIVATE)
        userId.set(sharedPreferences.getString("userId",""))
        authToken.set(sharedPreferences.getString("authToken",""))
        registrationForCheck.set(sharedPreferences.getString("registrationFor",""))

//        Log.d("regCheck", "checkUserLogin: ${registrationForCheck.get()}")

        if (userId.get()!=null && userId.get().isNullOrEmpty() && authToken.get()!=null && registrationForCheck.get()!=null){
            isLoading = true
            Toast.makeText(context, "Login Required...", Toast.LENGTH_SHORT).show()
        }
        else if(registrationForCheck.get().equals("Driver") || registrationForCheck.get().equals("DeliverPartner")){
            val intent = Intent(context,DriverRegistrationActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finishAffinity()
        }
        else if(registrationForCheck.get().equals("Restaurant")){
            val intent = Intent(context,RestaurantRegistrationActivity::class.java)
            context.startActivity(intent)
            (context as Activity).finishAffinity()
        }
    }





    /*---------------------------------REPO------------------------------------*/

    private val getAllRequiredDocsResponse : MutableLiveData<GetAllRequiredDocsResponse> = mainRepository.getAllRequiredDocsObservable()
    private val getAllRestaurantDocsResponse : MutableLiveData<GetAllRestaurantDocsResponse> = mainRepository.getAllRequiredRestaurantObservable()


    /*------------------------------------ SEND REQUEST TO REPO ----------------------*/
    private fun getAllRequiredDocs(title: String){
        mainRepository.getAllRequiredDocs(title);
    }

    private fun getAllRequiredRestaurantDocs(title: String){
        mainRepository.getAllRequiredRestaurantDocs(title)
    }

    /*----------------------------------- GET ALL OBSERVABLE -------------------------*/
    fun getAppRequiredDocsObservable() : MutableLiveData<GetAllRequiredDocsResponse>{
        return getAllRequiredDocsResponse
    }

    fun getAllRequiredRestaurantObservable() : MutableLiveData<GetAllRestaurantDocsResponse>{
        return getAllRestaurantDocsResponse
    }
}