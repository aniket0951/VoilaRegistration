package com.voila.voilasailor.notification.NotificationRepository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.notification.NetworkResponse.DeleteNotificationResponse
import com.voila.voilasailor.notification.NetworkResponse.NotificationResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NotificationRepository {

    private val notificationResponseLiveData: MutableLiveData<NotificationResponse> = MutableLiveData()
    private val deleteNotificationLiveData:MutableLiveData<DeleteNotificationResponse> = MutableLiveData()


    // creating observable
    fun notificationObservable():MutableLiveData<NotificationResponse>{
        return  notificationResponseLiveData
    }

    fun deleteNotificationObservable(): MutableLiveData<DeleteNotificationResponse>{
        return deleteNotificationLiveData
    }

     /*---- calling api ----*/
     @SuppressLint("CheckResult")
     fun getNotifications(request_token:String){
         val apiClient = ApiClient.RetrofitCall.retrofit
         val getNotificationObservable : Observable<NotificationResponse?>? = apiClient.getSailorNotification(request_token)

         getNotificationObservable
             ?.subscribeOn(Schedulers.io())
             ?.unsubscribeOn(Schedulers.computation())
             ?.observeOn(AndroidSchedulers.mainThread())
             ?.subscribe({
                 notificationResponseLiveData.postValue(it)
             },{
                 notificationResponseLiveData.postValue(null)
             })

     }

    @SuppressLint("CheckResult")
    fun removeNotifications(notification_id:String){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val deleteNotificationObservable : Observable<DeleteNotificationResponse?>? = apiClient.removeNotification(notification_id)

        deleteNotificationObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                deleteNotificationLiveData.postValue(it)
            },{
                deleteNotificationLiveData.postValue(null)
            })

    }
}