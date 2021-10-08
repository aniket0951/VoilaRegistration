package com.voila.voilasailor.Helper

import android.R
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.voila.voilasailor.restaurantRegistration.Util.removeToast
import com.voila.voilasailor.restaurantRegistration.Util.toasts
import android.net.ConnectivityManager

import android.net.NetworkInfo





public class Helper {



    object DialogsUtils {
        fun showProgressDialog(context: Context?, message: String?): ProgressDialog {
            val m_Dialog = ProgressDialog(context)
            m_Dialog.setMessage(message)
            m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            m_Dialog.setCancelable(true)
            m_Dialog.show()
            return m_Dialog
        }
    }


    object getAuthToken{
        fun authToken(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("voila", Context.MODE_PRIVATE)
            return sharedPreferences.getString("authToken", "").toString()
        }
    }

    object getRestaurantId{
        fun restaurantId(context: Context) : String {
            val sharedPreferences = context.getSharedPreferences("voila",Context.MODE_PRIVATE)
            return sharedPreferences.getString("userId","").toString()
        }
    }



    object onSuccessMSG{
        var toast : Toast? = null
        fun onSuccess(context: Context,msg : String){
            toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
            toast?.setText(msg)
            toast?.duration ?: Toast.LENGTH_SHORT
            toast?.show()
           // context.toasts(msg)
        }

        fun removeMsg(context: Context){
            toast?.cancel()
        }
    }


    object onFailedMSG{
        fun onFailed(context: Context,s:String){
            context.toasts(s)
        }
    }

    /*------- remove the toast --------- */
    object RemoveMSG{
        fun removeMSG(context: Context){
           // context.removeToast()
        }
    }

    object OnInternetConnectionFailMSG{
        fun NetworkError(activity: Activity) {
            val snack = Snackbar.make(
                activity.findViewById(R.id.content),
                "Network not available!",
                Snackbar.LENGTH_LONG
            )
            val params = snack.view.layoutParams as FrameLayout.LayoutParams
            params.setMargins(15, 10, 15, 20)
            snack.show()
        }
    }

    /*-------- check delivery partner login or driver login to send api ----------*/
    object IsPartner{
        fun isDeliveryPartner(context: Context) : Boolean{
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("voila", Context.MODE_PRIVATE)
            val dp = sharedPreferences.getString("registrationFor","")

            if (dp == "DeliverPartner"){
                return true
            }
           return false
        }
    }

    object NetworkUtil {
        const val TYPE_NOT_CONNECTED = 0
        const val TYPE_WIFI = 1
        const val TYPE_MOBILE = 2
        const val NETWORK_STATUS_NOT_CONNECTED = 3
        const val NETWORK_STATUS_WIFI = 4
        const val NETWORK_STATUS_MOBILE = 5
        fun getConnectivityStatus(context: Context): Int {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI
                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE
            }
            return TYPE_NOT_CONNECTED
        }

        fun getConnectivityStatusString(context: Context): Int {
            val conn = getConnectivityStatus(context)
            var status = 0
            when (conn) {
                TYPE_WIFI -> {
                    status = NETWORK_STATUS_WIFI
                }
                TYPE_MOBILE -> {
                    status = NETWORK_STATUS_MOBILE
                }
                TYPE_NOT_CONNECTED -> {
                    status = NETWORK_STATUS_NOT_CONNECTED
                }
            }
            return status
        }
    }
}