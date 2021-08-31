package com.voila.voilasailor.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(var context: Context) : ViewModelProvider.NewInstanceFactory() {

    init {
        this.context = context
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(context) as T
    }
}