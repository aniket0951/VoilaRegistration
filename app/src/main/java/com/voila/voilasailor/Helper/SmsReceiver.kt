package com.voila.voilasailor.Helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log


class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {

        val data = intent!!.extras

        val pdus = data!!["pdus"] as Array<*>?

        for (i in pdus!!.indices) {
            val smsMessage = SmsMessage.createFromPdu(pdus!![i] as ByteArray)
            val sender = smsMessage.displayOriginatingAddress
            //ToDo check your sender
            val messageBody = smsMessage.displayMessageBody
            val updateTokenIntent = Intent("UPDATE_OTP")
            updateTokenIntent.putExtra("msg", getVerificationCode(messageBody))
            p0!!.sendBroadcast(updateTokenIntent)
        }
    }

    private fun getVerificationCode(message: String?): String? {
        if (message == null) {
            return null
        }
        val index = message.indexOf("is")
        val index_last_length = message.indexOf(".")
        if (index != -1) {
            val start = index + 3
            return message.substring(0, 4)
            Log.d("SMSReads", "getVerificationCode: " + message.substring(0,4))
        }
        Log.d("SMSReads", "getVerificationCode: " + message.substring(0,4))

        return null
    }
}