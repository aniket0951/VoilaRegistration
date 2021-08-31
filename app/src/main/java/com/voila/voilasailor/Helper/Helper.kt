package com.voila.voilasailor.Helper

import android.R
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar
import com.voila.voilasailor.restaurantRegistration.Util.toast


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
        fun onSuccess(context: Context,msg : String){
            context.toast(msg)
        }
    }


    object onFailedMSG{
        fun onFailed(context: Context,s:String){
            context.toast(s)
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

}