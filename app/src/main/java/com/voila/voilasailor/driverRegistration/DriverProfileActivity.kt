package com.voila.voilasailor.driverRegistration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.ActivityDriverProfileBinding
import com.voila.voilasailor.driverRegistration.Adapter.DriverProfileAdapter
import com.voila.voilasailor.driverRegistration.Model.*
import com.voila.voilasailor.driverRegistration.NetworkResponse.DriverRequestedInfoResponse
import com.voila.voilasailor.driverRegistration.ViewModelFactory.DriverProfileViewModelFactory
import com.voila.voilasailor.driverRegistration.ViewModelListener.DriverProfileViewModelListener
import com.voila.voilasailor.driverRegistration.viewModel.DriverProfileViewModel
import com.voila.voilasailor.restaurantRegistration.RestaurantRegistrationActivity
import com.voila.voilasailor.restaurantRegistration.Util.toasts
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DriverProfileActivity : AppCompatActivity(), DriverProfileViewModelListener,
    DriverProfileAdapter.OnBasicUpdateClick, DriverProfileAdapter.OnAddressUpdateClick,
    DriverProfileAdapter.OnVehicleUpdateClick, DriverProfileAdapter.OnVehicleFrontClick,
    DriverProfileAdapter.OnVehicleListenerClick, DriverProfileAdapter.OnInsuranceListenerClick,
    DriverProfileAdapter.OnPermitListenerClick, DriverProfileAdapter.OnAdharFrontListenerClick,
    DriverProfileAdapter.OnAdharBackListener, DriverProfileAdapter.OnLiceneceFrontListener,
    DriverProfileAdapter.OnLiceneceBackListener {

    lateinit var binding : ActivityDriverProfileBinding
    lateinit var profileViewModel : DriverProfileViewModel
    var jsonObject : JsonObject = JsonObject()
    private var selectedImage : Uri? = null
    var isKYCImage : Boolean = false

    lateinit var adapter : DriverProfileAdapter

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    var docName = ObservableField<String>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_profile)
        profileViewModel = ViewModelProviders.of(this,DriverProfileViewModelFactory(this)).get(DriverProfileViewModel::class.java)
        binding.profile = profileViewModel
        profileViewModel.listener = this
        binding.executePendingBindings()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.imageView.setOnClickListener {
            if (binding.recyclerView.isVisible){

                binding.recyclerViewParentLayout.visibility = View.GONE
                binding.textParentLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE

                binding.toolbar.title = "Driver Profile "
                binding.toolbar.titleMarginStart = 65


            }
            else{
                val intent = Intent(this,DriverRegistrationActivity::class.java)
                startActivity(intent)


            }
        }

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
            it?.let { uri ->
                selectedImage = uri
                // Log.d("imageUri", "onCreate: $uri")
                // binding.dishImage.setImageURI(uri)
                uploadImage()
            }
            if (it == null){
                Helper.onFailedMSG.onFailed(this,"Do not use camera....")
            }
        }


    }


    override fun onBasicInfo() {
        profileViewModel.getRequestedInfoObservable()
            .observe(this, Observer {
                if(it!=null){
                    if (it.result){
                        showBasicInfo(it)
                    }
                    else{
                        profileViewModel.dismissDialog()
                        Helper.onFailedMSG.onFailed(this,"Information not found please check")
                    }
                }
            })
    }

    private fun showBasicInfo(it: DriverRequestedInfoResponse) {
        var list : ArrayList<BasicRequestedInfo> = ArrayList()

        val mainList : ArrayList<RequestedInfo> = it.requestedInfo as ArrayList<RequestedInfo>

        for (count in mainList.indices){
            list = mainList[count].basicRequestedInfo as ArrayList<BasicRequestedInfo>
        }

        adapter = DriverProfileAdapter(this)
        adapter.showBasicInfo(list)
        binding.recyclerView.adapter = adapter

        binding.recyclerViewParentLayout.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.textParentLayout.visibility = View.GONE

        adapter.setOnBasicClickListener(this)

        binding.toolbar.title = "Basic Information"
        binding.toolbar.titleMarginStart = 65
        profileViewModel.dismissDialog()
    }

    override fun onAddressInfo() {
       profileViewModel.getRequestedInfoObservable()
           .observe(this, Observer {
               if (it!=null){
                   if (it.result){
                       showAddressInfo(it)
                   }
                   else{
                       profileViewModel.dismissDialog()
                       Helper.onFailedMSG.onFailed(this,"Address Information not found")
                   }
               }
           })
    }

    private fun showAddressInfo(it: DriverRequestedInfoResponse) {
        var list : ArrayList<AddressRequestedInfo> = ArrayList()

        for (count in it.requestedInfo.indices){
            list = it.requestedInfo[count].addressRequestedInfo as ArrayList<AddressRequestedInfo>
        }

        adapter = DriverProfileAdapter(this)
        adapter.showAddressInfo(list)
        binding.recyclerView.adapter = adapter

        binding.recyclerViewParentLayout.visibility = View.VISIBLE
        binding.textParentLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE

        binding.toolbar.title = "Address Information"
        binding.toolbar.titleMarginStart = 65

        adapter.setOnAddressClick(this)

        profileViewModel.dismissDialog()
    }

    override fun onKYCInfo() {
        profileViewModel.getRequestedInfoObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        showKYCInfo(it)
                    }
                    else{
                        profileViewModel.dismissDialog()
                        Helper.onFailedMSG.onFailed(this,"KYC Information not found")
                    }
                }
            })
    }

    private fun showKYCInfo(it: DriverRequestedInfoResponse) {
        var list : ArrayList<KycRequestedInfo> = ArrayList()

        for (count in it.requestedInfo.indices){
            list = it.requestedInfo[count].kycRequestedInfo as ArrayList<KycRequestedInfo>
        }

        adapter = DriverProfileAdapter(this)
        adapter.showKYCInfo(list)
        binding.recyclerView.adapter = adapter

        binding.recyclerViewParentLayout.visibility = View.VISIBLE
        binding.textParentLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE

        binding.toolbar.title = "KYC Details"
        binding.toolbar.titleMarginStart = 65

        adapter.setOnAdharFrontClick(this)
        adapter.setOnAdharBackClick(this)
        adapter.setOnLicenceFrontClick(this)
        adapter.setOnLicenceBackClick(this)

        profileViewModel.dismissDialog()
    }

    override fun onVehicleInfo() {
        profileViewModel.getRequestedInfoObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        showVehicleInfo(it)
                    }
                    else{
                        profileViewModel.dismissDialog()
                        Helper.onFailedMSG.onFailed(this,"Vehicle Information not found")
                    }
                }
            })
    }

    private fun showVehicleInfo(it: DriverRequestedInfoResponse) {

        var list : ArrayList<VehicleRequestedInfo> = ArrayList()

        for (count in it.requestedInfo.indices){
            list = it.requestedInfo[count].vehicleRequestedInfo as ArrayList<VehicleRequestedInfo>
        }

        adapter = DriverProfileAdapter(this)
        adapter.showVehicleInfo(list)
        binding.recyclerView.adapter = adapter

        binding.recyclerViewParentLayout.visibility = View.VISIBLE
        binding.textParentLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE

        binding.toolbar.title = "Vehicle Information"
        binding.toolbar.titleMarginStart = 65

        adapter.setOnVehicleClick(this)
        adapter.setOnVehicleRCClick(this)
        adapter.setInsuranceClick(this)
        adapter.setPermitClick(this)

        profileViewModel.dismissDialog()
    }

    override fun onVehicleDocuments() {
        profileViewModel.getRequestedInfoObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        showVehicleDocumentsInfo(it)
                    }
                    else{
                        profileViewModel.dismissDialog()
                        Helper.onFailedMSG.onFailed(this,"Vehicle Documents Information not found")
                    }
                }
            })
    }

    override fun onBasicInfoUpdate() {
        profileViewModel.updatePersonalInformationObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        profileViewModel.dismissDialog()
                        toasts(it.message)
                        profileViewModel.moveOnHomeScreen()
                    }
                    else{
                        profileViewModel.dismissDialog()
                        toasts("Information not updated please try again")
                    }
                }
            })
    }

    override fun onAddressInfoUpdate() {
        profileViewModel.updateAddressDetailsObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        profileViewModel.dismissDialog()
                        toasts(it.message)
                        profileViewModel.moveOnHomeScreen()
                    }
                    else{
                        profileViewModel.dismissDialog()
                        toasts("Information not updated please try again")
                    }
                }
            })
    }

    override fun onVehicleInfoUpdate() {
        profileViewModel.updateVehicleDetailsObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        profileViewModel.dismissDialog()
                        toasts(it.message)
                        profileViewModel.moveOnHomeScreen()
                    }
                    else{
                        profileViewModel.dismissDialog()
                        toasts("Information not updated please try again")
                    }
                }
            })
    }

    override fun onKYCInfoUpdate() {
        GlobalScope.launch(Dispatchers.Main) {
            profileViewModel.updateKYCDocumentObservable()
                .observe(this@DriverProfileActivity, Observer {
                    if (it!=null){
                        if (it.result){
                            profileViewModel.dismissDialog()
                            toasts(it.message)
                            profileViewModel.moveOnHomeScreen()
                        }
                        else{
                            profileViewModel.dismissDialog()
                            toasts("Information not updated please try again")
                        }
                    }
                })
        }
    }

    @DelicateCoroutinesApi
    override fun onVehicleDocumentUpdate() {
        GlobalScope.launch(Dispatchers.Main) {
            profileViewModel.updateVehicleProfileObservable()
                .observe(this@DriverProfileActivity, Observer {
                    if (it!=null){
                        if (it.result){
                            profileViewModel.dismissDialog()
                            toasts(it.message)
                            profileViewModel.moveOnHomeScreen()
                        }
                        else{
                            profileViewModel.dismissDialog()
                            toasts("Information not updated please try again")
                        }
                    }
                })
        }
    }

    private fun showVehicleDocumentsInfo(it: DriverRequestedInfoResponse) {
        var list : ArrayList<VehicleProfilePic> = ArrayList()

        for (count in it.requestedInfo.indices){
            list = it.requestedInfo[count].vehicleProfilePic as ArrayList<VehicleProfilePic>
        }

        adapter = DriverProfileAdapter(this)
        adapter.showVehicleProfilePic(list)
        binding.recyclerView.adapter = adapter

        binding.recyclerViewParentLayout.visibility = View.VISIBLE
        binding.textParentLayout.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE

        binding.toolbar.title = "Vehicle Documents"
        binding.toolbar.titleMarginStart = 65

        adapter.setOnVehicleFrontClick(this)
        adapter.setOnVehicleRCClick(this)
        adapter.setInsuranceClick(this)
        adapter.setPermitClick(this)

        profileViewModel.dismissDialog()
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        if (!binding.recyclerView.isVisible){
            startActivity(Intent(this,DriverRegistrationActivity::class.java))

        }
        else{

            binding.recyclerViewParentLayout.visibility = View.GONE
            binding.textParentLayout.visibility = View.VISIBLE

            binding.recyclerView.visibility = View.GONE
        }
    }

    override fun onBasicUpdateClick(position: Int) {
        jsonObject.addProperty("full_name",adapter.basicInfo[position].full_name)
        jsonObject.addProperty("email",adapter.basicInfo[position].email)
        jsonObject.addProperty("contact_number",adapter.basicInfo[position].contact_number)
        jsonObject.addProperty("date_of_birth",adapter.basicInfo[position].date_of_birth)
        jsonObject.addProperty("request_token",Helper.getAuthToken.authToken(this))

        profileViewModel.updateBasicInformation(jsonObject)
    }

    override fun onAddressClick(position: Int) {
        jsonObject.addProperty("house_number",adapter.addressInfo[position].house_number)
        jsonObject.addProperty("building_name",adapter.addressInfo[position].building_name)
        jsonObject.addProperty("street_name",adapter.addressInfo[position].street_name)
        jsonObject.addProperty("landmark",adapter.addressInfo[position].landmark)
        jsonObject.addProperty("state",adapter.addressInfo[position].state)
        jsonObject.addProperty("district",adapter.addressInfo[position].district)
        jsonObject.addProperty("pin_code",adapter.addressInfo[position].pin_code)
        jsonObject.addProperty("request_token",Helper.getAuthToken.authToken(this))

        profileViewModel.updateAddressInfo(jsonObject)
    }

    override fun onVehicleClick(position: Int) {

        jsonObject.addProperty("vehicle_rto_registration_number",adapter.vehicleInfo[position].vehicle_RTO_registration_number)
        jsonObject.addProperty("vehicle_rc_number",adapter.vehicleInfo[position].vehicle_rc_number)
        jsonObject.addProperty("vehicle_colour",adapter.vehicleInfo[position].vehicle_colour)
        jsonObject.addProperty("vehicle_make_year",adapter.vehicleInfo[position].vehicle_make_year)
        jsonObject.addProperty("vehicle_type",adapter.vehicleInfo[position].vehicle_type)
        jsonObject.addProperty("vehicle_brand",adapter.vehicleInfo[position].vehicle_brand)
        jsonObject.addProperty("vehicle_model",adapter.vehicleInfo[position].vehicle_model)
        jsonObject.addProperty("request_token",Helper.getAuthToken.authToken(this))

        if (adapter.vehicleInfo[position].vehicle_type.equals("Auto",true)){
            jsonObject.addProperty("global_vehicle_id","1")
        }
        else{
            jsonObject.addProperty("global_vehicle_id","2")
        }

        profileViewModel.updateVehicleInfo(jsonObject)
    }

    override fun onVehicleFrontClick(position: Int) {
        docName.set("vehicle_front_photo")
        openImage()
    }

    private fun openImage() {
        cropActivityResultLauncher.launch(null)
    }
    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(16,9)
                .getIntent(this@DriverProfileActivity)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                RestaurantRegistrationActivity.REQUEST_CODE_IMAGE_PICKER -> {
                    selectedImage = data?.data
                   // Log.d("imagePath", "onActivityResult: image path $selectedImage")
                    uploadImage()
                }
            }
        }

    }


    companion object{

        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun uploadImage() {
        if (selectedImage==null){
            Helper.onFailedMSG.onFailed(this,"Please select the image")
        }
        else{

            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return

            val file = File(cacheDir,selectedImage.toString())
            if (!file.parentFile.exists()){
                file.parentFile.mkdirs()
                //  Log.d("imageUri", "uploadImage: from parent file $file")
            }
            else if (!file.exists()) {
                // Log.d("imageUri", "uploadImage: from  file $file")
                file.createNewFile()
            }

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            // val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            // Log.d("fileLength", "uploadImage before: ${file.length()}")


            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(this@DriverProfileActivity, file) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(2_097_152) // 2 MB
                }
                // create RequestBody instance from file
                val requestFile = RequestBody.create(
                    contentResolver.getType(selectedImage!!)?.let { it.toMediaTypeOrNull() },
                    compressedImageFile
                )

                // Log.d("fileLength", "uploadImage after select: ${compressedImageFile.length()}")

                // MultipartBody.Part is used to send also the actual file name
                val body = MultipartBody.Part.createFormData("image", compressedImageFile.name, requestFile)


                val name: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    docName.get()
                        .toString()
                )

                if (isKYCImage){
                    profileViewModel.updateKYCInfo(body,name)
                }
                else{
                    profileViewModel.updateVehicleDocuments(body,name)
                }
            }
            profileViewModel.showProgress()
        }
    }

    override fun onVehicleRcClick(position: Int) {
        docName.set("vehicle_rc")
        openImage()
    }

    override fun onVehicleInsuranceClick(position: Int) {
        docName.set("vehicle_insurance")
        openImage()
    }

    override fun onPermitClick(position: Int) {
        docName.set("vehicle_permit")
        openImage()
    }

    override fun aadharClick(position: Int) {
        isKYCImage = true
        docName.set("aadhaar_front_photo")
        openImage()
    }

    override fun onAdharBackListener(position: Int) {
        isKYCImage = true
        docName.set("aadhaar_back_photo")
        openImage()
    }

    override fun onLicenceFrontClick(position: Int) {
        isKYCImage = true
        docName.set("licence_front_photo")
        openImage()
    }

    override fun onLicenceBackClick(position: Int) {
        isKYCImage = true
        docName.set("licence_back_photo")
        openImage()
    }
}