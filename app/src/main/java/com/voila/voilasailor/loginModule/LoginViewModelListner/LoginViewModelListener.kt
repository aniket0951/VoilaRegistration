package com.voila.voilasailor.loginModule.LoginViewModelListner

interface LoginViewModelListener {

    fun onOTPSendSuccess()
    fun onOTPSendFailed()
    fun onFiledEmpty()
    fun onVerifyOtpSuccess()
    fun onVerifyOtpFailed(s: String)
    fun onUserSavedLocally()
    fun onResendOtpSuccessfully()
}