package com.example.voilaregistration.viewModel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.voilaregistration.MainViewModelListener.MainViewModelListener
import com.example.voilaregistration.NetworkResponse.GetAllRequiredDocsResponse
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import com.example.voilaregistration.Repository.MianRepository
import com.example.voilaregistration.driverRegistration.DriverRegistrationActivity
import com.example.voilaregistration.restaurantRegistration.RestaurantRegistrationActivity
import java.util.*


class MainActivityViewModel(var context: Context) : ViewModel(){


    lateinit var listener: MainViewModelListener
    var mainRepository: MianRepository = MianRepository()
    private val userId = ObservableField<String>()
    private val authToken = ObservableField<String>()
     val registrationFor = ObservableField<String>()
    val registrationForCheck = ObservableField<String>()

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
        Log.d("clickCheck", "registerAsRestaurant: register restaurant click")
    }

    fun checkUserLogin(){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("voila", Context.MODE_PRIVATE)
        userId.set(sharedPreferences.getString("userId",""))
        authToken.set(sharedPreferences.getString("authToken",""))
        registrationForCheck.set(sharedPreferences.getString("registrationFor",""))

        if (userId.get()!=null && userId.get().isNullOrEmpty() && authToken.get()!=null && registrationForCheck.get()!=null){
            Toast.makeText(context, "Login Required...", Toast.LENGTH_SHORT).show()
        }
        else if(registrationForCheck.get().equals("Driver")){
            val intent = Intent(context,DriverRegistrationActivity::class.java)
            context.startActivity(intent)
        }
        else if(registrationForCheck.get().equals("Restaurant")){
            val intent = Intent(context,RestaurantRegistrationActivity::class.java)
            context.startActivity(intent)
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