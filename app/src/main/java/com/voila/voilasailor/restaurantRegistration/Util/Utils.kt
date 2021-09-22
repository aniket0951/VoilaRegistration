package com.voila.voilasailor.restaurantRegistration.Util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.voila.voilasailor.Helper.Helper
import com.google.android.material.snackbar.Snackbar


fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun View.snackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok")
        {
            snackbar.dismiss()
        }
    }.show()
}
 val toasts : Toast? = null
fun Context.toasts(message: CharSequence) =
   Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun removeToast() = toasts!!.cancel()

fun Context.authToken() = Helper.getAuthToken.authToken(this)

