package com.voila.voilasailor.driverRegistration.viewModel

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.driverRegistration.NetworkResponse.ConformRateCardResponse
import com.voila.voilasailor.driverRegistration.NetworkResponse.CustomRateCardResponse
import com.voila.voilasailor.driverRegistration.NetworkResponse.SystemRateCardResponse
import com.voila.voilasailor.driverRegistration.Repository.RateCardRepository
import com.voila.voilasailor.driverRegistration.ViewModelListener.RateCardViewModelListener
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.ProfileListener
import com.voila.voilasailor.restaurantRegistration.Util.toasts

class RateCardViewModel(val context: Context): ViewModel() {
    lateinit var listener : RateCardViewModelListener
    private val rateCardRepository: RateCardRepository = RateCardRepository()
    lateinit var  dialog : ProgressDialog
    lateinit var jsonObject: JsonObject

    fun dismissDialog(){
        if (dialog!=null)dialog.dismiss()
    }

    fun showProgress(){
        dialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait....")
    }

    /*--- when driver use system rate card --*/
    fun useSystemRateCard(){
        showProgress()
        getSystemRateCard()
        listener.onSystemRateSelect()
    }

    /*----  when driver use custom rate card --*/
    fun useCustomRateCard(){
        showProgress()
        jsonObject = JsonObject()
        jsonObject.addProperty("driver_id", Helper.getRestaurantId.restaurantId(context))
        jsonObject.addProperty("request_token", Helper.getAuthToken.authToken(context))
        getCustomRateCard(jsonObject)
        listener.onCustomRateSelect()
    }

    fun changeRateCardType(){
        listener.onChangeRateCardType()
    }

    fun conformRateCard(min_rate:String, max_rate:String){
        showProgress()
        jsonObject = JsonObject()
        jsonObject.addProperty("driver_id",Helper.getRestaurantId.restaurantId(context))
        jsonObject.addProperty("min_rate",min_rate)
        jsonObject.addProperty("max_rate",max_rate)

        conformRateCard(jsonObject)
        listener.onConformRateCard()
    }

    fun conformCustomRateCard(min_rate: String, max_rate: String){
        showProgress()
        jsonObject = JsonObject()
        jsonObject.addProperty("driver_id",Helper.getRestaurantId.restaurantId(context))
        jsonObject.addProperty("min_rate",min_rate)
        jsonObject.addProperty("max_rate",max_rate)

        conformRateCard(jsonObject)
        listener.onConformCustomRateCard()
    }

    @SuppressLint("CommitPrefEdits")
    fun insertPrefOfRateCard(rateCardType:String){
        val sharedPreferences: SharedPreferences? =
            context.getSharedPreferences("voila", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()

        if (rateCardType == "systemRateCard"){
            editor.putString("IsEnable","systemRateCard")
            editor.apply()
        }
        else if (rateCardType == "customRateCard"){
            editor.putString("IsEnable", "customRateCard")
            editor.apply()
        }

    }

    /*------------------------------- INIT REPO ----------------*/
    private val getSystemRateCardLiveData : MutableLiveData<SystemRateCardResponse> = rateCardRepository.getSystemRateCardObservable()
    private val conformRateCardLiveData : MutableLiveData<ConformRateCardResponse> = rateCardRepository.conformRateCardObservable()
    private val customRateCardLiveData: MutableLiveData<CustomRateCardResponse> = rateCardRepository.customRateCardObservable()


    /*-----------------------------  CALLING REPO FUN ------------ */
    private fun getSystemRateCard(){
        rateCardRepository.getSystemRateCard(Helper.getAuthToken.authToken(context))
    }

    private fun conformRateCard(jsonObject: JsonObject){
        rateCardRepository.conformRateCard(jsonObject)
    }

    private fun getCustomRateCard(jsonObject: JsonObject){
        rateCardRepository.customRateCard(jsonObject)
    }

    /*-------------------------- Observ    -----*/
    fun getSystemRateCardObservable() : MutableLiveData<SystemRateCardResponse>{
        return getSystemRateCardLiveData
    }

    fun conformRateCardObservable():MutableLiveData<ConformRateCardResponse>{
        return conformRateCardLiveData
    }
    fun customRateCardObservable():MutableLiveData<CustomRateCardResponse>{
        return customRateCardLiveData
    }
}