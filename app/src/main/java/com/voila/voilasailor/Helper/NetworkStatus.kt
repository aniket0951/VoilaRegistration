package com.voila.voilasailor.Helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo


@SuppressLint("StaticFieldLeak")
object  NetworkStatus {
    private val instance: NetworkStatus = NetworkStatus
    var context: Context? = null
    private var connectivityManager: ConnectivityManager? = null
    var wifiInfo: NetworkInfo? = null
    var mobileInfo:NetworkInfo? = null
    var connected = false

    fun getInstance(ctx: Context): NetworkStatus? {
        context = ctx.applicationContext
        return instance
    }


    fun isOnline(): Boolean {
        try {
            connectivityManager = context
                ?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
            val networkInfo = connectivityManager!!.activeNetworkInfo
            connected = networkInfo != null && networkInfo.isAvailable &&
                    networkInfo.isConnected
            return connected
        } catch (e: Exception) {
            println("CheckConnectivity Exception: " + e.message)
          //  Log.v("connectivity", e.toString())
        }
        return connected
    }
}