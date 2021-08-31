package com.voila.voilasailor.restaurantRegistration.RestaurantFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.MenuCardViewModel

class MenuCardViewModelFactory(var context: Context) : ViewModelProvider.NewInstanceFactory() {

    init {
        this.context = context
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuCardViewModel(context) as T
    }
}