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
import com.voila.voilasailor.driverRegistration.DriverProfileActivity
import com.voila.voilasailor.driverRegistration.NetworkResponse.*
import com.voila.voilasailor.driverRegistration.Repository.DriverRegistrationRepository
import com.voila.voilasailor.driverRegistration.ViewModelListener.DriverRegistrationViewModelListener
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Subscription

class DriverRegistrationViewModel(var context: Context):ViewModel() {

    var driverRegistrationRepository : DriverRegistrationRepository = DriverRegistrationRepository()

    lateinit var listener : DriverRegistrationViewModelListener
    private lateinit var progressDail : ProgressDialog
    val progressStatus = ObservableField<String>()
    val progressDailMSG = ObservableField<String>()

    //dismiss progress dail
    fun dismissProgressDail(){
        if (progressDail!=null) progressDail.dismiss()
    }

    fun showProgressDai(){
        progressDail = Helper.DialogsUtils.showProgressDialog(context,progressDailMSG.get().toString())
    }

    fun showProfileInformation(){
        val intent = Intent(context,DriverProfileActivity::class.java)
        context.startActivity(intent)
    }

    //track driver registration
    fun _trackDriverRegistration(){
        if (NetworkStatus.getInstance(context)!!.isOnline()){

            progressDail = Helper.DialogsUtils.showProgressDialog(context,"Please wait we are track your  registration process")
            trackDriverRegistrationProcess(Helper.getAuthToken.authToken(context))
            listener.onDriverRegistrationTrackSuccessfully()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    //add user registration process
    fun addUserRegistrationProcess(jsonObject: JsonObject){
        showProgressDai()
        if (NetworkStatus.getInstance(context)?.isOnline() == true) {
            when (progressStatus.get().toString()) {
                "0" -> {
                    addPersonalInformation(jsonObject)
                    listener.onAddPersonalInformationSuccess()
                }
                "1" -> {
                    addAddressDetails(jsonObject)
                    listener.onAddAddressSuccessfully()
                }
                "3" -> {
                    addVehicleDetails(jsonObject)
                    listener.onAddVehicleDetailsSuccessfully()
                }
            }
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    //add kyc documents
    fun _addKYCDocument(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){
       if (NetworkStatus.getInstance(context)?.isOnline()!!) {

          // GlobalScope.launch {  showProgressDai()}
           addKYCDocument(body, title, request_token)
           listener.onKYCDocumentUploadedSuccessfully()
       }
       else{
           Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
       }
    }

    //add vehicle profile details
    fun _addVehicleProfiel(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
           // GlobalScope.launch { showProgressDai() }
            addVehicleProfile(body, title, request_token)
            listener.onVehicleProfileUploadedSuccessfully()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    /*---------------------- CONTACT WITH REPO -------------------- */
    private val trackDriverRegistrationLiveData : MutableLiveData<TrackDriverRegistrationProccessResponse> = driverRegistrationRepository.trackDriverRegistrationObservable()
    private val addPersonalInformationLiveData : MutableLiveData<AddPersonalInformationResponse> = driverRegistrationRepository.addPersonalInformationObservable()
    private val addAddressLiveData : MutableLiveData<AddAddressResponse> = driverRegistrationRepository.addAddressDetailsObservable()
    private val addKYCDocumentLiveData : MutableLiveData<AddKYCDocumentResponse> = driverRegistrationRepository.addKYCDocumentObservable()
    private val addVehicleDetailsLiveData : MutableLiveData<AddVehicleDetailsResponse> = driverRegistrationRepository.addVehicleDetailsObservable()
    private val addVehicleProfileLiveData : MutableLiveData<AddVehicleProfileResponse> = driverRegistrationRepository.addVehicleProfileObservable()


    /*------------------------- CALLING REPO FUN ----------------------- */
    private fun trackDriverRegistrationProcess(request_token:String){
        driverRegistrationRepository.trackDriverRegistration(request_token)
    }

    private fun addPersonalInformation(jsonObject: JsonObject){
        driverRegistrationRepository.addPersonalInformation(jsonObject)
    }

    private fun addAddressDetails(jsonObject: JsonObject){
        driverRegistrationRepository.addAddressDetails(jsonObject)
    }

    private fun addKYCDocument(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){
        driverRegistrationRepository.addKYCDocument(body, title, request_token)
    }

    private fun addVehicleDetails(jsonObject: JsonObject){
        driverRegistrationRepository.addVehicleDetails(jsonObject)
    }

    private fun addVehicleProfile(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){
        driverRegistrationRepository.addVehicleProfile(body,title, request_token)
    }

    /*------------------------ OBSERVABLE -------------------- */
    fun trackDriverRegistrationProcessObservable() : MutableLiveData<TrackDriverRegistrationProccessResponse>{
        return trackDriverRegistrationLiveData
    }

    fun addPersonalInformationObservable() : MutableLiveData<AddPersonalInformationResponse>{
        return addPersonalInformationLiveData
    }

    fun addAddressDetailsObservable() : MutableLiveData<AddAddressResponse>{
        return addAddressLiveData
    }

    fun addKYCDocumentObservable() : MutableLiveData<AddKYCDocumentResponse>{
        return addKYCDocumentLiveData
    }

    fun addVehicleDetailsObservable() : MutableLiveData<AddVehicleDetailsResponse> {
        return addVehicleDetailsLiveData
    }

    fun addVehicleProfileObservable() : MutableLiveData<AddVehicleProfileResponse>{
        return addVehicleProfileLiveData
    }
}