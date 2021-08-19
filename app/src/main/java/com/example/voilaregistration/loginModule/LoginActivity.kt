package com.example.voilaregistration.loginModule

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.R
import com.example.voilaregistration.databinding.ActivityLoginBinding
import com.example.voilaregistration.loginModule.LoginViewModelFactory.LoginViewModelFactory
import com.example.voilaregistration.loginModule.LoginViewModelListner.LoginViewModelListener
import com.example.voilaregistration.loginModule.loginViewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(),LoginViewModelListener {

    lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel : LoginViewModel
    lateinit var progressBar : ProgressDialog
    lateinit var helper: Helper
    private val registrationFor  = ObservableField<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)
        binding.login = loginViewModel
        loginViewModel.listner = this
        binding.executePendingBindings()

        if (intent!=null){
            registrationFor.set(intent.getStringExtra("registrationFor"))
            Log.d("registrationFor", "onCreate: " + registrationFor.get())
        }

        val edit = arrayOf<EditText>(otp_edit_box1, otp_edit_box2, otp_edit_box3, otp_edit_box4)

        otp_edit_box1.addTextChangedListener(GenericTextWatcher(otp_edit_box1, edit))
        otp_edit_box2.addTextChangedListener(GenericTextWatcher(otp_edit_box2, edit))
        otp_edit_box3.addTextChangedListener(GenericTextWatcher(otp_edit_box3, edit))
        otp_edit_box4.addTextChangedListener(GenericTextWatcher(otp_edit_box4, edit))

    }

    // class to focus on the selected Edit Text
    class GenericTextWatcher(view: View, editText: Array<EditText>) :
        TextWatcher {
        private val editText: Array<EditText> = editText
        private val view: View = view
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (view.id) {
                R.id.otp_edit_box1 -> if (text.length == 1) editText[1].requestFocus()
                R.id.otp_edit_box2 -> if (text.length == 1) editText[2].requestFocus() else if (text.isEmpty()) editText[0].requestFocus()
                R.id.otp_edit_box3 -> if (text.length == 1) editText[3].requestFocus() else if (text.isEmpty()) editText[1].requestFocus()
                R.id.otp_edit_box4 -> if (text.isEmpty()) editText[2].requestFocus()
            }
        }

        override fun beforeTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}
        override fun onTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}

    }

    override fun onOTPSendSuccess() {
        progressBar =  Helper.DialogsUtils.showProgressDialog(this, "Sending Otp Please wait...")
        Log.d("sendOTP", "onOTPSendSuccess: otp send successfully")
        loginViewModel.sendOtpResponseObservable()
            .observe(this, Observer {
                if (it != null && it.result) {
                    if (it.details.equals("Insufficient Account Balance",ignoreCase = true)){
                        onOTPSendFailed()
                    }
                    enableVerifyOtpLayout()
                    loginViewModel.sessionId.set(it.details)
                }
                progressBar.dismiss()
            })
    }

    /*--after send the otp visible a verify otp*/
    private fun enableVerifyOtpLayout() {
        binding.signUpTextLayout.visibility = View.GONE
        binding.otpVerifyLayout.visibility = View.VISIBLE

        binding.phoneNumberLayot.visibility = View.GONE
        binding.getOtpTextLayout.visibility = View.VISIBLE
        binding.mobileNumberText.text = loginViewModel.mobileNumber.get()
    }

    override fun onOTPSendFailed() {
        Toast.makeText(this, "Otp not send", Toast.LENGTH_SHORT).show()
      //  Log.d("sendOTP", "onOTPSendFailed: otp send failed")
    }

    override fun onFiledEmpty() {
        Toast.makeText(this, "Please enter the otp..", Toast.LENGTH_SHORT).show()
    }

    override fun onVerifyOtpSuccess() {
        loginViewModel.verifyOtpObservable()
            .observe(this, Observer {
                if (it!=null ){
                    if (it.result) {
                       // progressBar.dismiss()
                        loginViewModel.saveUserLocally(it,registrationFor.get())
                    }
                    else{
                       loginViewModel.onOtpVerificationFailed(it.message)
                    }
                }
            })
    }


    override fun onVerifyOtpFailed(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onUserSavedLocally() {
        Toast.makeText(this, "user saved locally", Toast.LENGTH_SHORT).show()
    }
}