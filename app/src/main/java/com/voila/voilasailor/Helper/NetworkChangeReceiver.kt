package com.voila.voilasailor.Helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.voila.voilasailor.Helper.NetworkStatus.context

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val status: Int = Helper.NetworkUtil.getConnectivityStatusString(p0!!)
        if ("android.net.conn.CONNECTIVITY_CHANGE" == p1?.action) {
            if (status == Helper.NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                Helper.onFailedMSG.onFailed(p0,"Internet connection loss please check your connection")
            } else {
                Helper.onSuccessMSG.onSuccess(p0,"Connection established")
            }
        }
    }
}