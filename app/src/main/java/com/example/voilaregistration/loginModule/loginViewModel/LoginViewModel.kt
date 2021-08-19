package com.example.voilaregistration.loginModule.loginViewModel

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.loginModule.LoginRepository.LoginRepository
import com.example.voilaregistration.loginModule.LoginViewModelListner.LoginViewModelListener
import com.example.voilaregistration.loginModule.Model.UserDetail
import com.example.voilaregistration.loginModule.NetworkResponse.OtpVerificationResponse
import com.example.voilaregistration.loginModule.NetworkResponse.SendOtpResponse
import com.example.voilaregistration.restaurantRegistration.UI.RestaurantHomeScreenActivity
import com.google.firebase.messaging.FirebaseMessaging

class LoginViewModel(var context: Context) : ViewModel() {

    public var mobileNumber = ObservableField<String>()
    var sessionId = ObservableField<String>()
    var editOtp1 = ObservableField<String>()
    var editOtp2 = ObservableField<String>()
    var editOtp3 = ObservableField<String>()
    var editOtp4 = ObservableField<String>()

    var FCM_TOKEN = ObservableField<String>()
    lateinit var progressDialog: ProgressDialog

    lateinit var listner : LoginViewModelListener
    var loginRepository : LoginRepository = LoginRepository()

    //send otp
    fun sendOTP(){
        if(mobileNumber.get()!=null &&mobileNumber.get()?.isEmpty()!!){
            listner.onOTPSendFailed()
           // Log.d("sendOTP", "sendOTP: number empty")
        }
        else{
            listner.onOTPSendSuccess()
            mobileNumber.get()?.let { sendOtp(it) }
        }
    }

    //verify the  otp
    fun verifyTheOtp(){
        progressDialog = Helper.DialogsUtils.showProgressDialog(
            context,
            "Please wait we are verifying your otp"
        )
        if (editOtp1.get()!=null && editOtp1.get().isNullOrEmpty() && editOtp2.get()!=null && editOtp2.get().isNullOrEmpty()
            && editOtp3.get()!=null && editOtp3.get().isNullOrEmpty() && editOtp4.get()!=null && editOtp4.get().isNullOrEmpty()){

                progressDialog.dismiss()
                listner.onFiledEmpty()
           // Log.d("verifyOtp", "verifyTheOtp: please enter otp")
        }
        else{
            val otp = editOtp1.get()+editOtp2.get()+editOtp3.get()+editOtp4.get();
            sessionId.get()?.let { getFCMToken(otp, it, FCM_TOKEN.get().toString()) }
        }
    }

    //getting fcm token
    private fun getFCMToken(otp: String, sessionId: String, fcm_token: String) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FCM_TOKEN.set(task.result.toString())
                  otpVerify(otp, sessionId, fcm_token)
                    listner.onVerifyOtpSuccess()
                } else {
                    progressDialog.dismiss()
                    listner.onVerifyOtpFailed("Otp verification failed please try again")
                }
            }
    }


    //save user locally
    fun saveUserLocally(otpVerificationResponse: OtpVerificationResponse, get: String?) {
        var userDetailsList = ArrayList<UserDetail>()
        userDetailsList = otpVerificationResponse.userDetails as ArrayList<UserDetail>

        for (userDetails in userDetailsList) {
           val id : Int = userDetails.id
           val mobilNumber:String = userDetails.mobile_number
           val authToken:String = userDetails.auth_token
           insertPref(id,mobilNumber,authToken,get)
        }

        //move on home screen
        moveOnHomeScreen(get)
    }

    //move a home screen by check login for
    private fun moveOnHomeScreen(get: String?) {
        if (get.toString() == "Driver"){
           // val intent = Intent(context,Drive)
        }
        else{
            val intent = Intent(context,RestaurantHomeScreenActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun insertPref(id: Int, mobilNumber: String, authToken: String, get: String?) {
        val sharedPreferences: SharedPreferences? =
            context.getSharedPreferences("voila", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()

        editor.putString("userId", id.toString())
        editor.putString("authToken",authToken)
        editor.putString("mobileNumber",mobilNumber)
        editor.putString("registrationFor",get)
        editor.apply()
        if (progressDialog!=null) progressDialog.dismiss()
        listner.onUserSavedLocally()
    }

    //otp verification failed
    fun onOtpVerificationFailed(message: String) {
        if (progressDialog!=null)progressDialog.dismiss()
        listner.onVerifyOtpFailed(message)
    }





    /*-------------------------- REPO ---------------------------*/
    private val sendOtpResponseObservable : MutableLiveData<SendOtpResponse> = loginRepository.sendOtpObservable()
    private val otpVerificationObservable : MutableLiveData<OtpVerificationResponse> = loginRepository.otpVerifyObservable()

    /*---------------------------  CALLING REPO FUN ------------------------*/
    fun sendOtp(mobile_number: String){
        loginRepository.sendOtpOnMobileNumber(mobile_number)
    }

    //verify otp
    fun otpVerify(otp: String, sessionId: String, fcm_token: String){
        mobileNumber.get()?.let { loginRepository.verifyOtp(otp, sessionId, fcm_token, it) }
    }

    /*------------------- RESPONSE OBSERVABLE--------------------------------*/
    fun sendOtpResponseObservable() : MutableLiveData<SendOtpResponse>{
        return sendOtpResponseObservable
    }

    //verify otp observable
    fun verifyOtpObservable() : MutableLiveData<OtpVerificationResponse>{
        return otpVerificationObservable
    }
}