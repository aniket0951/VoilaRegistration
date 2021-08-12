package com.example.voilaregistration.loginModule.LoginViewModelListner

interface LoginViewModelListener {

    fun onOTPSendSuccess()
    fun onOTPSendFailed()
    fun onFiledEmpty()
    fun onVerifyOtpSuccess()
    fun onVerifyOtpFailed(s: String)
    fun onUserSavedLocally()
}