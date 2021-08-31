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

class DriverRegistrationRepository {

    private val trackDriverRegistrationLiveData : MutableLiveData<TrackDriverRegistrationProccessResponse> = MutableLiveData()
    private val addPersonalInformationLiveData : MutableLiveData<AddPersonalInformationResponse> = MutableLiveData()
    private val addAddressLiveData : MutableLiveData<AddAddressResponse> = MutableLiveData()
    private val addKYCDocumentLiveData : MutableLiveData<AddKYCDocumentResponse> = MutableLiveData()
    private val addVehicleDetailsLiveData : MutableLiveData<AddVehicleDetailsResponse> = MutableLiveData()
    private val addVehicleProfileLiveData : MutableLiveData<AddVehicleProfileResponse> = MutableLiveData()

    /*---------------------------  OBSERVABLE ----------------------------------*/

    //track driver registration
    fun trackDriverRegistrationObservable() : MutableLiveData<TrackDriverRegistrationProccessResponse>{
        return trackDriverRegistrationLiveData
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

    /*-------------------------------  CALLING REPO FUN ----------------------*/
    //to track driver registration process
    @SuppressLint("CheckResult")
    fun trackDriverRegistration(request_token:String){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val driverRegistrationObservable : Observable<TrackDriverRegistrationProccessResponse?>? = apiClient.trackDriverRegistrationProcess(WebServer.external_api_token,request_token)

        driverRegistrationObservable
            ?.subscribeOn(Schedulers.io())
            ?.unsubscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                trackDriverRegistrationLiveData.postValue(it)
            },{
                trackDriverRegistrationLiveData.postValue(null)
            })
    }

    //add personal information
    @SuppressLint("CheckResult")
    fun addPersonalInformation(jsonObject: JsonObject){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addPersonalInformationObservable : Observable<AddPersonalInformationResponse?>? = apiClient.addPersonalInformation(WebServer.external_api_token,jsonObject)

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

    //add address details
    @SuppressLint("CheckResult")
    fun addAddressDetails(jsonObject: JsonObject){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addAddressDetailsObservable : Observable<AddAddressResponse?>? = apiClient.addAddressDetails(WebServer.external_api_token,jsonObject)

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

    //add kyc document
    @SuppressLint("CheckResult")
    fun addKYCDocument(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addKYCDocumentObservable : Observable<AddKYCDocumentResponse?>? = apiClient.addKYCDocs(body,title, request_token)

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

    //add vehicle details
    @SuppressLint("CheckResult")
    fun addVehicleDetails(jsonObject: JsonObject){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addVehicleDetailsObservable : Observable<AddVehicleDetailsResponse?>? = apiClient.addVehicleDetails(WebServer.external_api_token,jsonObject)

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

    //add vehicle profile details
    @SuppressLint("CheckResult")
    fun addVehicleProfile(body: MultipartBody.Part, title: RequestBody, request_token: RequestBody){

        val apiClient = ApiClient.RetrofitCall.retrofit
        val addKYCDocumentObservable : Observable<AddVehicleProfileResponse?>? = apiClient.addVehicleProfile(body,title, request_token)

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