package com.voila.voilasailor.notification.NotificationFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voila.voilasailor.notification.viewModel.NotificationViewModel

class NotificationFactory(var context: Context) : ViewModelProvider.NewInstanceFactory() {

    init {
        this.context = context
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationViewModel(context) as T
    }
}