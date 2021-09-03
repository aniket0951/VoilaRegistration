package com.voila.voilasailor.driverRegistration

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.kofigyan.stateprogressbar.StateProgressBar
import com.theartofdev.edmodo.cropper.CropImage
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.ActivityDriverRegistrationBinding
import com.voila.voilasailor.driverRegistration.Adapter.DriverRegistrationAdapter
import com.voila.voilasailor.driverRegistration.NetworkResponse.TrackDriverRegistrationProccessResponse
import com.voila.voilasailor.driverRegistration.ViewModelFactory.DriverRegistrationViewModelFactory
import com.voila.voilasailor.driverRegistration.ViewModelListener.DriverRegistrationViewModelListener
import com.voila.voilasailor.driverRegistration.viewModel.DriverRegistrationViewModel
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.NeedToProcessComplete
import com.voila.voilasailor.restaurantRegistration.Util.getFileName
import com.voila.voilasailor.restaurantRegistration.Util.toast
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DriverRegistrationActivity : AppCompatActivity(), DriverRegistrationViewModelListener,
    DriverRegistrationAdapter.OnItemClickListener {

    lateinit var binding  : ActivityDriverRegistrationBinding
    lateinit var driverViewModel : DriverRegistrationViewModel
    lateinit var adapter : DriverRegistrationAdapter

    var jsonObject : JsonObject = JsonObject()
    private lateinit var docsType : String

    var vehicleType = ObservableField<String>()
    var isVehicleRegistration : Boolean = false
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>


    //list
    var mainNeedToProcessCompleteList = ArrayList<NeedToProcessComplete>()

    //get image path and adapter image position
    private var selectedImage : Uri? = null
    private var imagePosition : Int = 0

    //get title of doc name
    private var docsTitle = ObservableField<String>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_registration)
        driverViewModel = ViewModelProviders.of(this,DriverRegistrationViewModelFactory(this)).get(DriverRegistrationViewModel::class.java)
        binding.driver = driverViewModel
        driverViewModel.listener = this
        driverViewModel._trackDriverRegistration()

        binding.executePendingBindings()

        ActivityCompat.requestPermissions(
            this@DriverRegistrationActivity, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            100
        )

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract){
            it?.let { uri ->
                selectedImage = uri
               // Log.d("imageUri", "onCreate: $uri")
                // binding.dishImage.setImageURI(uri)
                uploadImage()
            }
            if (it == null){
                toast("Do not use camera....")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED) {
                // User checks permission.
            } else {
                Toast.makeText(this@DriverRegistrationActivity, "Permission is denied.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    override fun onDriverRegistrationTrackSuccessfully() {
       driverViewModel.trackDriverRegistrationProcessObservable()
           .observe(this, Observer {
               if (it!=null){
                   if (it.result){
                       binding.btnSaveConfirm.visibility = View.VISIBLE
                       driverViewModel.dismissProgressDail()
                       showRegistrationForm(it)
                   }
                   else{
                       binding.btnSaveConfirm.visibility = View.GONE
                       driverViewModel.dismissProgressDail()
                       Helper.onSuccessMSG.onSuccess(this,it.message)
                   }
               }
           })
    }

    override fun onAddPersonalInformationSuccess() {
        driverViewModel.addPersonalInformationObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        driverViewModel.dismissProgressDail()
                        Helper.onSuccessMSG.onSuccess(this,it.message)
                        driverViewModel._trackDriverRegistration()
                    }
                    else{
                        driverViewModel.dismissProgressDail()
                        Helper.onSuccessMSG.onSuccess(this,it.message)
                    }
                }
            })
    }

    override fun onAddAddressSuccessfully() {
        driverViewModel.addAddressDetailsObservable()
            .observe(this, Observer {
                if (it!=null){

                    if (it.result){
                        driverViewModel.dismissProgressDail()
                        Helper.onSuccessMSG.onSuccess(this,it.message)
                        driverViewModel._trackDriverRegistration()
                    }
                    else{
                        driverViewModel.dismissProgressDail()
                        Helper.onFailedMSG.onFailed(this,it.message)
                    }
                }
            })
    }

    override fun onKYCDocumentUploadedSuccessfully() {
        GlobalScope.launch(Dispatchers.Main) {

            driverViewModel.addKYCDocumentObservable()
                .observe(this@DriverRegistrationActivity, Observer {
                    if (it!=null){
                        if (it.result){
                            driverViewModel.dismissProgressDail()
                            Helper.onSuccessMSG.onSuccess(this@DriverRegistrationActivity,it.message)
                            driverViewModel._trackDriverRegistration()
                        }
                        else{
                            driverViewModel.dismissProgressDail()
                            Helper.onFailedMSG.onFailed(this@DriverRegistrationActivity,it.message)
                        }
                    }
                    else{
                        driverViewModel.dismissProgressDail()
                        Helper.onFailedMSG.onFailed(this@DriverRegistrationActivity,"Please try again")
                    }
                })
        }
    }

    override fun onAddVehicleDetailsSuccessfully() {
        driverViewModel.addVehicleDetailsObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        driverViewModel.dismissProgressDail()
                        Helper.onSuccessMSG.onSuccess(this,it.message)
                        driverViewModel._trackDriverRegistration()
                    }
                    else{
                        driverViewModel.dismissProgressDail()
                        Helper.onFailedMSG.onFailed(this,it.message)
                    }
                }
            })
    }

    override fun onVehicleProfileUploadedSuccessfully() {
        GlobalScope.launch(Dispatchers.Main){
            driverViewModel.addVehicleProfileObservable()
                .observe(this@DriverRegistrationActivity, Observer {
                    if (it!=null){
                        if (it.result){
                            driverViewModel.dismissProgressDail()
                            Helper.onSuccessMSG.onSuccess(this@DriverRegistrationActivity,it.message)
                            driverViewModel._trackDriverRegistration()
                        }
                        else{
                            driverViewModel.dismissProgressDail()
                            Helper.onFailedMSG.onFailed(this@DriverRegistrationActivity,it.message)
                        }
                    }
                })
        }
    }

    private fun setRegistrationTitle(it: TrackDriverRegistrationProccessResponse) {
        if (it.processCompleteStatus !=null){

            driverViewModel.progressStatus.set(it.processCompleteStatus.toString())
            when(it.processCompleteStatus){
                "0"->{
                    binding.stepBar.setMaxStateNumber(StateProgressBar.StateNumber.FIVE)
                    binding.stepBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
                    binding.stepBar.visibility = View.VISIBLE
                    binding.toolbar.title = "Basic Details"
                    driverViewModel.progressDailMSG.set("Please wait we are adding your basic information..")
                }
                "1" ->{

                    binding.stepBar.setMaxStateNumber(StateProgressBar.StateNumber.FIVE)
                    binding.stepBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
                    binding.stepBar.visibility = View.VISIBLE
                    binding.toolbar.title = "Address Details"
                    driverViewModel.progressDailMSG.set("Please wait we are adding your address details..")
                }
                "2" -> {

                    binding.stepBar.setMaxStateNumber(StateProgressBar.StateNumber.FIVE)
                    binding.stepBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
                    binding.stepBar.visibility = View.VISIBLE
                    binding.toolbar.title = "Document Details"
                    driverViewModel.progressDailMSG.set("Please wait we are adding your document..")

                }
                "3" -> {

                    binding.stepBar.setMaxStateNumber(StateProgressBar.StateNumber.FIVE)
                    binding.stepBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
                    binding.stepBar.visibility = View.VISIBLE
                    isVehicleRegistration = true
                    binding.toolbar.title = "Vehicle Details"
                    driverViewModel.progressDailMSG.set("Please wait we are adding your vehicle details..")

                }
                "4" -> {

                    binding.stepBar.setMaxStateNumber(StateProgressBar.StateNumber.FIVE)
                    binding.stepBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE)
                    binding.stepBar.visibility = View.VISIBLE
                    binding.toolbar.title = "Vehicle Images"
                    driverViewModel.progressDailMSG.set("Please wait we are adding your vehicle images..")
                }
                else -> {

                    binding.stepBar.setMaxStateNumber(StateProgressBar.StateNumber.FIVE)
                    binding.stepBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE)
                    binding.stepBar.visibility = View.VISIBLE
                    binding.toolbar.title = "Voila Registration"
                    driverViewModel.progressDailMSG.set("Please wait ...")
                }
            }
        }
        else{
            binding.stepBar.setMaxStateNumber(StateProgressBar.StateNumber.FIVE)
            binding.stepBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
            binding.stepBar.visibility = View.VISIBLE
            binding.toolbar.title = "Account under review"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRegistrationForm(it: TrackDriverRegistrationProccessResponse) {
        setRegistrationTitle(it)
        driverViewModel.dismissProgressDail()
        //storing list
        mainNeedToProcessCompleteList = it.needToProcessComplete as ArrayList<NeedToProcessComplete>

        //check list is not null
        if (mainNeedToProcessCompleteList != null && mainNeedToProcessCompleteList.isNotEmpty()) {

            binding.profileText.visibility = View.GONE

            for (count in mainNeedToProcessCompleteList.indices) {
                docsType = mainNeedToProcessCompleteList[count].required_docs_type
            }

            when (docsType) {
                "image" -> {

                    val numberOfColumns = 2
                    binding.recyclerView.layoutManager = GridLayoutManager(this, numberOfColumns)

                    adapter = DriverRegistrationAdapter(this)
                    adapter.addRestaurantImages(mainNeedToProcessCompleteList)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    binding.btnSaveConfirm.visibility = View.GONE
                    adapter.setOnItemClickListener(this)

                }
                "text" -> {

                    binding.recyclerView.layoutManager = LinearLayoutManager(this)


                    adapter = DriverRegistrationAdapter(this)
                    adapter.trackRegistrationProcess(mainNeedToProcessCompleteList)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                   submitAllDetails(adapter)
                }
                else -> {
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)


                    adapter = DriverRegistrationAdapter(this)
                    adapter.trackRegistrationProcess(mainNeedToProcessCompleteList)
                    binding.recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    submitAllDetails(adapter)
                }
            }

        } else if (!it.isVerify) {
            showUnderReviewAccount(it)
        }
        else if (it.isVerify && it.isVerify!=null){
            showUnderReviewAccount(it)
        }
        else {
            Helper.onFailedMSG.onFailed(this,"Internal Server Error!!!")
        }
    }

    //submit all details
    private fun submitAllDetails(adapter: DriverRegistrationAdapter) {
        binding.btnSaveConfirm.setOnClickListener {
            Log.d("btnConform", "showRegistrationForm: click")
            for (count in adapter.needToProcessComplete.indices) {

            var title: String = adapter.needToProcessComplete[count].required_docs_name
            var editVal: String = adapter.needToProcessComplete[count].editText

                title = title.replace(" ".toRegex(), "_").toLowerCase()

                if (adapter.needToProcessComplete[count].autoCompleteTextView!=null && adapter.needToProcessComplete[count].autoCompleteTextView.isNotEmpty()) {

                    vehicleType.set(adapter.needToProcessComplete[count].autoCompleteTextView)
                }

                if (adapter.needToProcessComplete[count].editText == null || adapter.needToProcessComplete[count].editText.isEmpty()){

                    editVal = adapter.needToProcessComplete[count].autoCompleteTextView
                }

                jsonObject.addProperty(title, editVal)

            }

            if (isVehicleRegistration){
                jsonObject.addProperty("vehicle_type",vehicleType.get().toString())
                if (vehicleType.get().toString() == "Auto"){
                    jsonObject.addProperty("global_vehicle_id","1")
                } else{
                    jsonObject.addProperty("global_vehicle_id", "2")
                }
            }

            //adding request token in json object
            jsonObject.addProperty("request_token", Helper.getAuthToken.authToken(this))


            driverViewModel.addUserRegistrationProcess(jsonObject)
        }
    }

    private fun showUnderReviewAccount(it: TrackDriverRegistrationProccessResponse) {

        binding.stepBar.visibility = View.GONE

        val underReviewURL : String = it.verificationData
        Glide.with(this).load(underReviewURL).into(binding.accountUnderReviewImg)

        binding.underReviewText.text = it.message

        binding.accountUnderReviewImg.visibility = View.VISIBLE
        binding.underReviewText.visibility = View.VISIBLE
        binding.btnSaveConfirm.visibility = View.GONE

        binding.recyclerViewParentLayout.visibility = View.GONE
        binding.layoutAccountUnderReview.visibility = View.VISIBLE

        binding.profileText.visibility = View.VISIBLE
    }

    override fun onItemClick(position: Int) {
        imagePosition = position
        val isUploadFirst = ObservableField<String>()
        var title : String = mainNeedToProcessCompleteList[position].required_docs_name
        title = title.replace(" ".toRegex(), "_").toLowerCase()

        docsTitle.set(title)
        isUploadFirst.set(mainNeedToProcessCompleteList[0].required_docs_name)

        if (position == 0) {
            openImageChooser(position)

            //Log.d("postionDoc", "onItemClick: $position  $title")
        }
        else {
           Helper.onFailedMSG.onFailed(this,"Please upload first " + " " + isUploadFirst.get().toString() + " document...." )
        }

    }

    private fun openImageChooser(position: Int) {
        cropActivityResultLauncher.launch(null)
    }

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(16,9)
                .getIntent(this@DriverRegistrationActivity)
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
                REQUEST_CODE_IMAGE_PICKER -> {
                    selectedImage = data?.data
                    //  Log.d("imagePath", "onActivityResult: image path $selectedImage")
                    uploadImage()
                }
            }
        }

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
                val compressedImageFile = Compressor.compress(this@DriverRegistrationActivity, file) {
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

                // Log.d("fileLength", "uploadImage after select: ${reduceFile.name + " " +  reduceFile.length()}")

                // MultipartBody.Part is used to send also the actual file name
                val body = MultipartBody.Part.createFormData("image", compressedImageFile.name, requestFile)


                val name: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    docsTitle.get()
                        .toString()
                )

                val requestToken: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    Helper.getAuthToken.authToken(this@DriverRegistrationActivity).toString()
                )

                if (driverViewModel.progressStatus.get().toString() == "4"){
                    driverViewModel._addVehicleProfiel(body,name,requestToken)
                }
                else{
                    driverViewModel._addKYCDocument(body,name,requestToken)
                }
            }
            driverViewModel.showProgressDai()
        }
    }

    private fun getBitmapFile(reduceBitmap: Bitmap?): File {

        val file : File = File(cacheDir, selectedImage?.let { contentResolver.getFileName(it) })
        file.createNewFile()
        val bos : ByteArrayOutputStream = ByteArrayOutputStream()
        reduceBitmap?.compress(Bitmap.CompressFormat.JPEG,0,bos)
        val bitmapData : ByteArray = bos.toByteArray()

        val fos : FileOutputStream = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return file
    }

    companion object{

        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}