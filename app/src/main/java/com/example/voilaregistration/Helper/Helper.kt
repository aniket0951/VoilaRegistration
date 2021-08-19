package com.example.voilaregistration.Helper

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView


public class Helper {

   public fun setProgressDialog(context: Context, message: String) {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        val window: Window = dialog.window!!
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutParams
        }
    }

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

    object DialogDismiss{
        fun dismissProgressDialog(progressDialog: ProgressDialog){
            if (progressDialog!=null) progressDialog.dismiss()
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

}