package com.example.voilaregistration.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.voilaregistration.loginModule.loginViewModel.LoginViewModel

class MainViewModelFactory(var context: Context) : ViewModelProvider.NewInstanceFactory() {

    init {
        this.context = context
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(context) as T
    }
}