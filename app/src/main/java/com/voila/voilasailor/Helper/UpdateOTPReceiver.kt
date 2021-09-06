package com.voila.voilasailor.Helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.voila.voilasailor.loginModule.LoginActivity

class UpdateOTPReceiver : BroadcastReceiver() {

    private var mListener: LoginActivity.SmsListener? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val msg = intent.getStringExtra("msg")
            mListener?.messageReceived(msg)
        }

    }
    fun bindListener(listener: LoginActivity.SmsListener) {
        mListener = listener
    }

}