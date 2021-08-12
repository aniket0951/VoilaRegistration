package com.example.voilaregistration.restaurantRegistration.RestaurantFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.voilaregistration.restaurantRegistration.restaurantViewModel.RestaurantRegistrationViewModel

class RestaurantViewModelFactory(var context: Context) : ViewModelProvider.NewInstanceFactory() {
    init {
        this.context = context
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RestaurantRegistrationViewModel(context) as T
    }
}