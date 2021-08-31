package com.voila.voilasailor.loginModule.LoginViewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voila.voilasailor.loginModule.loginViewModel.LoginViewModel

class LoginViewModelFactory(var context: Context) : ViewModelProvider.NewInstanceFactory() {

   init {
       this.context = context
   }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(context) as T
    }
}