package com.voila.voilasailor.driverRegistration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.ActivityDriverProfileBinding
import com.voila.voilasailor.driverRegistration.ViewModelFactory.DriverProfileViewModelFactory
import com.voila.voilasailor.driverRegistration.ViewModelListener.DriverProfileViewModelListener
import com.voila.voilasailor.driverRegistration.viewModel.DriverProfileViewModel

class DriverProfileActivity : AppCompatActivity(), DriverProfileViewModelListener {

    lateinit var binding : ActivityDriverProfileBinding
    lateinit var profileViewModel : DriverProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_profile)
        profileViewModel = ViewModelProviders.of(this,DriverProfileViewModelFactory(this)).get(DriverProfileViewModel::class.java)
        binding.profile = profileViewModel
        profileViewModel.listener = this
        binding.executePendingBindings()
    }

    override fun onRequestedInformation() {

    }

    override fun onBasicInfo() {
        profileViewModel.getRequestedInfoObservable()
            .observe(this, Observer {
                if(it!=null){
                    if (it.result){

                    }
                    else{
                        profileViewModel.dismissDialog()
                        Helper.onFailedMSG.onFailed(this,"Information not found please check")
                    }
                }
            })
    }

    override fun onAddressInfo() {
        TODO("Not yet implemented")
    }

    override fun onKYCInfo() {
        TODO("Not yet implemented")
    }

    override fun onVehicleInfo() {
        TODO("Not yet implemented")
    }

    override fun onVehicleDocuments() {
        TODO("Not yet implemented")
    }
}