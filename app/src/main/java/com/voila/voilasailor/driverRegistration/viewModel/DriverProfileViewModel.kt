package com.voila.voilasailor.driverRegistration.viewModel

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.Helper.NetworkStatus
import com.voila.voilasailor.driverRegistration.NetworkResponse.DriverRequestedInfoResponse
import com.voila.voilasailor.driverRegistration.Repository.DriverProfileRepository
import com.voila.voilasailor.driverRegistration.ViewModelListener.DriverProfileViewModelListener

class DriverProfileViewModel(val context: Context) : ViewModel() {

    lateinit var listener : DriverProfileViewModelListener

    var driverProfileRepository : DriverProfileRepository = DriverProfileRepository()
    private val isSelected = ObservableField<String>()
   lateinit var  dialog : ProgressDialog
   private val dialogMSG = ObservableField<String>()

    fun dismissDialog(){
       if (dialog!=null)dialog.dismiss()
    }

    fun isBasicInformation(){
        isSelected.set("Basic")
        showSelectedInformation()
    }

    fun isAddressInformation(){
        isSelected.set("Address")
        showSelectedInformation()
    }

    fun isKYCInformation(){
        isSelected.set("KYC")
        showSelectedInformation()
    }

    fun isVehicleInformation(){
        isSelected.set("Vehicle")
        showSelectedInformation()
    }

    fun isVehicleDocuments(){
        isSelected.set("Vehicle Document")
        showSelectedInformation()
    }

    private fun showSelectedInformation() {
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            dialog = Helper.DialogsUtils.showProgressDialog(context, dialogMSG.get().toString())
            when(isSelected.get().toString()){

                "Basic" -> {
                    getRequestedInfo(Helper.getAuthToken.authToken(context))
                    listener.onBasicInfo()
                }

                "Address" -> {
                    getRequestedInfo(Helper.getAuthToken.authToken(context))
                    listener.onAddressInfo()
                }
                "KYC" -> {
                    getRequestedInfo(Helper.getAuthToken.authToken(context))
                    listener.onKYCInfo()
                }
                "Vehicle" -> {
                    getRequestedInfo(Helper.getAuthToken.authToken(context))
                    listener.onVehicleInfo()
                }
                "Vehicle Document" -> {
                    getRequestedInfo(Helper.getAuthToken.authToken(context))
                    listener.onVehicleDocuments()
                }
            }
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }


    /*--------------------------------- INIT Repo ------------------------------- */
    private val getRequestedLiveData : MutableLiveData<DriverRequestedInfoResponse> = driverProfileRepository.getRequestedObservable()


    /*-----------------------------------   Calling repo fun ----------------------------*/
    fun getRequestedInfo(requested_token:String){
        driverProfileRepository.getDriverRequestedInformation(requested_token)
    }

    /*----------------------------- Observable -------------------------------- */
    fun getRequestedInfoObservable() : MutableLiveData<DriverRequestedInfoResponse>{
        return getRequestedLiveData
    }
}