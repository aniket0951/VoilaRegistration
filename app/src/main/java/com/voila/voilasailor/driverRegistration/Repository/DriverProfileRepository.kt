package com.voila.voilasailor.driverRegistration.Repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.voila.voilasailor.API.ApiClient
import com.voila.voilasailor.API.WebServer
import com.voila.voilasailor.driverRegistration.NetworkResponse.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class DriverProfileRepository() {

    private val getRequestedLiveData : MutableLiveData<DriverRequestedInfoResponse> = MutableLiveData()
    private val addPersonalInformationLiveData : MutableLiveData<AddPersonalInformationResponse> = MutableLiveData()
    private val addAddressLiveData : MutableLiveData<AddAddressResponse> = MutableLiveData()
    private val addKYCDocumentLiveData : MutableLiveData<AddKYCDocumentResponse> = MutableLiveData()
    private val addVehicleDetailsLiveData : MutableLiveData<AddVehicleDetailsResponse> = MutableLiveData()
    private val addVehicleProfileLiveData : MutableLiveData<AddVehicleProfileResponse> = MutableLiveData()



    /*-----------------------------  Observable -------------------------------*/
    fun getRequestedObservable() : MutableLiveData<DriverRequestedInfoResponse>{
        return getRequestedLiveData
    }

    //add personal information
    fun addPersonalInformationObservable() : MutableLiveData<AddPersonalInformationResponse>{
        return addPersonalInformationLiveData
    }

    //add address details
    fun addAddressDetailsObservable() : MutableLiveData<AddAddressResponse>{
        return addAddressLiveData
    }

    //add kyc document
    fun addKYCDocumentObservable() : MutableLiveData<AddKYCDocumentResponse>{
        return addKYCDocumentLiveData
    }

    //add vehicle detail
    fun addVehicleDetailsObservable() : MutableLiveData<AddVehicleDetailsResponse>{
        return addVehicleDetailsLiveData
    }

    //add vehicle profile
    fun addVehicleProfileObservable() : MutableLiveData<AddVehicleProfileResponse>{
        return addVehicleProfileLiveData
    }

    /*-----------------------------  Calling Fun -------------------------------*/

    @SuppressLint("CheckResult")
    fun getDriverRequestedInformation(requested_token:String){
        val apiClient = ApiClient.RetrofitCall.retrofit
        val getDriverRequestedObservable : Observable<DriverRequestedInfoResponse>? = apiClient.getAllRequestedInformation(WebServer.external_api_token,requested_token)

        getDriverRequestedObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                getRequestedLiveData.postValue(it)
            },{
                getRequestedLiveData.postValue(null)
            })
    }

    //update personal information
    @SuppressLint("CheckResult")
    fun updatePersonalInformation(jsonObject: JsonObject){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addPersonalInformationObservable : Observable<AddPersonalInformationResponse?>? = apiClient.updatePersonalInformation(WebServer.external_api_token,jsonObject)

        addPersonalInformationObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                addPersonalInformationLiveData.postValue(it)
            },{
                addPersonalInformationLiveData.postValue(null)
            })
    }

    //update address details
    @SuppressLint("CheckResult")
    fun updateAddressDetails(jsonObject: JsonObject){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addAddressDetailsObservable : Observable<AddAddressResponse?>? = apiClient.updateAddressInformation(WebServer.external_api_token,jsonObject)

        addAddressDetailsObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                addAddressLiveData.postValue(it)
            },{
                addAddressLiveData.postValue(null)
            })
    }

    //update kyc document
    @SuppressLint("CheckResult")
    fun updateKYCDocument(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addKYCDocumentObservable : Observable<AddKYCDocumentResponse?>? = apiClient.updateKYCInformation(body,title, request_token)

        addKYCDocumentObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                addKYCDocumentLiveData.postValue(it)
            },{
                addKYCDocumentLiveData.postValue(null)
            })
    }

    //update vehicle details
    @SuppressLint("CheckResult")
    fun updateVehicleDetails(jsonObject: JsonObject){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addVehicleDetailsObservable : Observable<AddVehicleDetailsResponse?>? = apiClient.updateVehicleInformation(WebServer.external_api_token,jsonObject)

        addVehicleDetailsObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                addVehicleDetailsLiveData.postValue(it)
            },{
                addVehicleDetailsLiveData.postValue(null)
            })
    }

    //update vehicle profile details
    @SuppressLint("CheckResult")
    fun updateVehicleProfile(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addKYCDocumentObservable : Observable<AddVehicleProfileResponse?>? = apiClient.updateVehicleDocuments(body,title, request_token)

        addKYCDocumentObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                addVehicleProfileLiveData.postValue(it)
            },{
                addVehicleProfileLiveData.postValue(null)
            })
    }

}