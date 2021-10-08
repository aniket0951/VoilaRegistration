package com.voila.voilasailor.driverRegistration.ViewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voila.voilasailor.driverRegistration.viewModel.RateCardViewModel

class RateCardFactory(var context: Context) : ViewModelProvider.NewInstanceFactory() {

    init {
        this.context = context
    }


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RateCardViewModel(context) as T
    }
}