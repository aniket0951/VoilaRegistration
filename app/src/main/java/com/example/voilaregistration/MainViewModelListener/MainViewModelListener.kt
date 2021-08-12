package com.example.voilaregistration.MainViewModelListener

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.voilaregistration.viewModel.MainActivityViewModel

interface MainViewModelListener  {

    fun onSuccess();
    fun onFailed();
    fun onDriverRequiredDocs()
    fun onRestaurantRequiredDocs()
}