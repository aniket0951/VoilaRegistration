package com.example.voilaregistration.loginModule.LoginRepository

import androidx.lifecycle.MutableLiveData
import com.example.voilaregistration.API.ApiClient
import com.example.voilaregistration.API.WebServer
import com.example.voilaregistration.loginModule.NetworkResponse.OtpVerificationResponse
import com.example.voilaregistration.loginModule.NetworkResponse.SendOtpResponse
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class LoginRepository {

    private val sendOtpMutableLiveData : MutableLiveData<SendOtpResponse> = MutableLiveData()
    private val verifyOtpLiveData : MutableLiveData<OtpVerificationResponse> = MutableLiveData()

    /*----------------------------- LIVE DATA OBSERVABLE-----------------------*/

    fun sendOtpObservable() : MutableLiveData<SendOtpResponse>{
        return sendOtpMutableLiveData
    }

    fun otpVerifyObservable():MutableLiveData<OtpVerificationResponse>{
        return verifyOtpLiveData
    }

    /*------------------------------ SEND REQUEST TO REPO -------------------------*/
    fun sendOtpOnMobileNumber(mobile_number: String){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val sentOtpResponseObservable : Observable<SendOtpResponse?>? = apiClient.sendOtp(
            mobile_number,
            WebServer.external_api_token
        )
        sentOtpResponseObservable!!.subscribeOn(Schedulers.io())?.unsubscribeOn(Schedulers.computation())?.observeOn(
            AndroidSchedulers.mainThread()
        )
                ?.subscribe({
                    sendOtpMutableLiveData.postValue(it)
                }, {
                    sendOtpMutableLiveData.postValue(null)
                })
    }

    //verify the otp
    fun verifyOtp(otp: String, sessionId: String, fcm_token: String, mobile_number: String){
        val jsonObject = JsonObject()
        jsonObject.addProperty("mobile_number", mobile_number)
        jsonObject.addProperty("otp", otp)
        jsonObject.addProperty("session_id", sessionId)
        jsonObject.addProperty("fcm_token", fcm_token)

        val apiClient = ApiClient.RetrofitCall.retrofit
        val verifyOtpObservable : Observable<OtpVerificationResponse> = apiClient.verifyOtp(WebServer.external_api_token,jsonObject)
        verifyOtpObservable.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                verifyOtpLiveData.postValue(it)
            },{
                verifyOtpLiveData.postValue(null)
            })
    }
}