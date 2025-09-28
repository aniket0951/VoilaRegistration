package com.voila.voilasailor.notification.viewModel

import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.notification.NetworkResponse.DeleteNotificationResponse
import com.voila.voilasailor.notification.NetworkResponse.NotificationResponse
import com.voila.voilasailor.notification.NotificationListener.NotificationListener
import com.voila.voilasailor.notification.NotificationRepository.NotificationRepository

class NotificationViewModel(var context: Context):ViewModel() {
    lateinit var listener: NotificationListener

    private val notificationRepository : NotificationRepository = NotificationRepository()
    private lateinit var progressDialog: ProgressDialog


    fun dismissProgressDai(){
        if (progressDialog!= null) progressDialog.dismiss()
    }


    fun _getNotifications(request_token: String){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait we are getting notifications..")
        getNotifications(request_token)
        listener.onNotificationSuccess()
    }

    fun _removeNotification(notification_id: String){
        removeNotification(notification_id)
        listener.onNotificationDeleted()
    }


     /* ---------------------  IN REPO ---------------------------- */
     private val notificationResponseLiveData: MutableLiveData<NotificationResponse> = notificationRepository.notificationObservable()
    private val deleteNotificationLiveData:MutableLiveData<DeleteNotificationResponse> = notificationRepository.deleteNotificationObservable()


     /* --------------------- calling a function of repo ----------------------- */
     // notifications
     fun getNotifications(request_token: String){
         notificationRepository.getNotifications(request_token)
     }

    fun removeNotification(notification_id:String){
        notificationRepository.removeNotifications(notification_id)
    }


    /* -------------------- get observable ------------------------ */

    // notification observable
    fun getNotificationObservable(): MutableLiveData<NotificationResponse>{
        return notificationResponseLiveData
    }

    fun deleteNotificationObservable(): MutableLiveData<DeleteNotificationResponse>{
        return deleteNotificationLiveData
    }
}