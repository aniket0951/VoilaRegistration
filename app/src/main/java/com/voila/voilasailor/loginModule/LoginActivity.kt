package com.voila.voilasailor.loginModule

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.ActivityLoginBinding
import com.voila.voilasailor.loginModule.LoginViewModelFactory.LoginViewModelFactory
import com.voila.voilasailor.loginModule.LoginViewModelListner.LoginViewModelListener
import com.voila.voilasailor.loginModule.loginViewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),LoginViewModelListener {

    lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel : LoginViewModel
    lateinit var progressBar : ProgressDialog
    lateinit var helper: Helper
    private val registrationFor  = ObservableField<String>()

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)
        binding.login = loginViewModel
        loginViewModel.listner = this
        binding.executePendingBindings()

        val spannable = SpannableString("  By providing my phone number, I hereby agree & accept the Terms of Service & Privacy Policy in use of the Voila app.")
        val ss = SpannableString(spannable)
        val ssb = SpannableStringBuilder(spannable)

        val fcsRed = ForegroundColorSpan(R.color.homeTitle)


        ssb.setSpan(fcsRed, 60, 94, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.privacyPolice.text = ssb

        binding.privacyPolice.makeLinks(
            Pair("Terms of Service", View.OnClickListener {
                val url = "https://voila-sailor.flycricket.io/terms.html"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }),
            Pair("Privacy Policy", View.OnClickListener {
                val url = "https://voila-sailor.flycricket.io/terms.html"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }))

        if (intent!=null){
            registrationFor.set(intent.getStringExtra("registrationFor"))
            //Log.d("registrationFor", "onCreate: " + registrationFor.get())
        }

        val edit = arrayOf<EditText>(otp_edit_box1, otp_edit_box2, otp_edit_box3, otp_edit_box4)

        otp_edit_box1.addTextChangedListener(GenericTextWatcher(otp_edit_box1, edit))
        otp_edit_box2.addTextChangedListener(GenericTextWatcher(otp_edit_box2, edit))
        otp_edit_box3.addTextChangedListener(GenericTextWatcher(otp_edit_box3, edit))
        otp_edit_box4.addTextChangedListener(GenericTextWatcher(otp_edit_box4, edit))

    }

    private fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = -1
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                @SuppressLint("ResourceAsColor")
                override fun updateDrawState(textPaint: TextPaint) {

                    textPaint.color = R.color.homeTitle

                    textPaint.isUnderlineText = true
                }

                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
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
                R.id.otp_edit_box2 -> if (text.length == 1) editText[2].requestFocus()
                else if (text.isEmpty()) editText[0].requestFocus()
                R.id.otp_edit_box3 -> if (text.length == 1) editText[3].requestFocus()
                else if (text.isEmpty()) editText[1].requestFocus()
                R.id.otp_edit_box4 -> if (text.isEmpty()) editText[2].requestFocus()
            }
        }

        override fun beforeTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}
        override fun onTextChanged(arg0: CharSequence?, arg1: Int, arg2: Int, arg3: Int) {}

    }

    override fun onOTPSendSuccess() {
        progressBar =  Helper.DialogsUtils.showProgressDialog(this, "Sending Otp Please wait...")
      //  Log.d("sendOTP", "onOTPSendSuccess: otp send successfully")
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
        binding.privacyPolice.visibility = View.GONE
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
        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onResendOtpSuccessfully() {
        loginViewModel.sendOtpResponseObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        loginViewModel.dismissDialog()
                        loginViewModel.sessionId.set(it.details)
                        Helper.onSuccessMSG.onSuccess(this,it.message)
                    }
                    else{
                        Helper.onFailedMSG.onFailed(this,"Resend otp failed please try again...")
                    }
                }
            })
    }
}