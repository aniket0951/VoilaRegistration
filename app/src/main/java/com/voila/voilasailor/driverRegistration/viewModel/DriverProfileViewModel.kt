package com.voila.voilasailor.driverRegistration.viewModel

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.Helper.NetworkStatus
import com.voila.voilasailor.driverRegistration.DriverRegistrationActivity
import com.voila.voilasailor.driverRegistration.NetworkResponse.*
import com.voila.voilasailor.driverRegistration.Repository.DriverProfileRepository
import com.voila.voilasailor.driverRegistration.ViewModelListener.DriverProfileViewModelListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DriverProfileViewModel(val context: Context) : ViewModel() {

    lateinit var listener : DriverProfileViewModelListener

    var driverProfileRepository : DriverProfileRepository = DriverProfileRepository()
    private val isSelected = ObservableField<String>()
   lateinit var  dialog : ProgressDialog
   private val dialogMSG = ObservableField<String>()

    fun dismissDialog(){
       if (dialog!=null)dialog.dismiss()
    }

    fun showProgress(){
        dialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait....")
    }

    fun moveOnHomeScreen(){
        context.startActivity(Intent(context,DriverRegistrationActivity::class.java))
    }

    fun isBasicInformation(){
        isSelected.set("Basic")
        showSelectedInformation()
        dialogMSG.set("Please wait we are getting basic information")
       // listener.onBasicInfo()
    }

    fun isAddressInformation(){
        isSelected.set("Address")
        showSelectedInformation()
        dialogMSG.set("Please wait we are getting address information")
    }

    fun isKYCInformation(){
        isSelected.set("KYC")
        showSelectedInformation()
        dialogMSG.set("Please wait we are getting kyc information")

    }

    fun isVehicleInformation(){
        isSelected.set("Vehicle")
        showSelectedInformation()
        dialogMSG.set("Please wait we are getting vehicle information")
    }

    fun isVehicleDocuments(){
        isSelected.set("Vehicle Document")
        showSelectedInformation()
        dialogMSG.set("Please wait we are getting vehicle documents information")
    }

    private fun showSelectedInformation() {
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            dialog = Helper.DialogsUtils.showProgressDialog(context, "please wait...")
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

    fun updateBasicInformation(jsonObject: JsonObject){
        dialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait.....")
        updatePersonalInformation(jsonObject)
        listener.onBasicInfoUpdate()
    }

    fun updateAddressInfo(jsonObject: JsonObject){
        dialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait.....")
        updateAddressDetails(jsonObject)
        listener.onAddressInfoUpdate()
    }

    fun updateVehicleInfo(jsonObject: JsonObject){
        dialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait.....")
        updateVehicleDetails(jsonObject)
        listener.onVehicleInfoUpdate()
    }

    fun updateKYCInfo(body: MultipartBody.Part, title: RequestBody){
//        dialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait...")

        val requestToken: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            Helper.getAuthToken.authToken(context)
        )

        updateKYCDocument(body, title, requestToken)
        listener.onKYCInfoUpdate()
    }

    fun updateVehicleDocuments(body: MultipartBody.Part, title: RequestBody){
      //  dialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait...")

        val requestToken: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            Helper.getAuthToken.authToken(context)
        )

        updateVehicleProfile(body, title, requestToken)
        listener.onVehicleDocumentUpdate()
    }

    /*--------------------------------- INIT Repo ------------------------------- */
    private val getRequestedLiveData : MutableLiveData<DriverRequestedInfoResponse> = driverProfileRepository.getRequestedObservable()
    private val addPersonalInformationLiveData : MutableLiveData<AddPersonalInformationResponse> = driverProfileRepository.addPersonalInformationObservable()
    private val addAddressLiveData : MutableLiveData<AddAddressResponse> = driverProfileRepository.addAddressDetailsObservable()
    private val addKYCDocumentLiveData : MutableLiveData<AddKYCDocumentResponse> = driverProfileRepository.addKYCDocumentObservable()
    private val addVehicleDetailsLiveData : MutableLiveData<AddVehicleDetailsResponse> = driverProfileRepository.addVehicleDetailsObservable()
    private val addVehicleProfileLiveData : MutableLiveData<AddVehicleProfileResponse> = driverProfileRepository.addVehicleProfileObservable()


    /*-----------------------------------   Calling repo fun ----------------------------*/
    fun getRequestedInfo(requested_token:String){
        driverProfileRepository.getDriverRequestedInformation(requested_token)
    }

    private fun updatePersonalInformation(jsonObject: JsonObject){
        driverProfileRepository.updatePersonalInformation(jsonObject)
    }

    private fun updateAddressDetails(jsonObject: JsonObject){
        driverProfileRepository.updateAddressDetails(jsonObject)
    }

    private fun updateKYCDocument(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){
        driverProfileRepository.updateKYCDocument(body, title, request_token)
    }

    private fun updateVehicleDetails(jsonObject: JsonObject){
        driverProfileRepository.updateVehicleDetails(jsonObject)
    }

    private fun updateVehicleProfile(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){
        driverProfileRepository.updateVehicleProfile(body,title, request_token)
    }

    /*----------------------------- Observable -------------------------------- */
    fun getRequestedInfoObservable() : MutableLiveData<DriverRequestedInfoResponse>{
        return getRequestedLiveData
    }

    fun updatePersonalInformationObservable() : MutableLiveData<AddPersonalInformationResponse>{
        return addPersonalInformationLiveData
    }

    fun updateAddressDetailsObservable() : MutableLiveData<AddAddressResponse>{
        return addAddressLiveData
    }

    fun updateKYCDocumentObservable() : MutableLiveData<AddKYCDocumentResponse>{
        return addKYCDocumentLiveData
    }

    fun updateVehicleDetailsObservable() : MutableLiveData<AddVehicleDetailsResponse> {
        return addVehicleDetailsLiveData
    }

    fun updateVehicleProfileObservable() : MutableLiveData<AddVehicleProfileResponse>{
        return addVehicleProfileLiveData
    }
}