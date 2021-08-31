package com.voila.voilasailor.restaurantRegistration

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.Model.RequireRestaurantDocs
import com.voila.voilasailor.Model.RestaurantOwnerRequiredDoc
import com.voila.voilasailor.NetworkResponse.GetAllRestaurantDocsResponse
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.ActivityRestaurantRegistrationBinding
import com.voila.voilasailor.restaurantRegistration.Adpter.RestaurantDetailsAdapter
import com.voila.voilasailor.restaurantRegistration.RestaurantFactory.RestaurantViewModelFactory
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.NeedToProcessComplete
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.TrackRegistrationProcessResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.RestaurantViewModelListener
import com.voila.voilasailor.restaurantRegistration.Util.getFileName
import com.voila.voilasailor.restaurantRegistration.Util.snackbar
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.RestaurantRegistrationViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.activity_restaurant_registration.*
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


class RestaurantRegistrationActivity : AppCompatActivity(),RestaurantViewModelListener,
    RestaurantDetailsAdapter.OnItemClickListener {

    lateinit var restaurantViewModel  : RestaurantRegistrationViewModel
    lateinit var binding : ActivityRestaurantRegistrationBinding

    lateinit var progressDialog : ProgressDialog
    var jsonObject : JsonObject = JsonObject()
    lateinit var docsType : String

    //adapter
     private lateinit var restaurantDetailsAdapter: RestaurantDetailsAdapter


     //list
     var mainNeedToProcessCompleteList = ArrayList<NeedToProcessComplete>()

    //get image path and adapter image position
    private var selectedImage : Uri? = null
    private var imagePosition : Int = 0

    //get title of doc name
    var docsTitle = ObservableField<String>()

    var isShowSnackBar : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_restaurant_registration)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_registration)
        restaurantViewModel = ViewModelProviders.of(this, RestaurantViewModelFactory(this)).get(RestaurantRegistrationViewModel::class.java)
        restaurantViewModel.listener = this
        binding.restaurant = restaurantViewModel
        binding.executePendingBindings()

        //restaurantViewModel.showRegistrationForm()
        restaurantViewModel.trackRegistrationProcess()

        ActivityCompat.requestPermissions(
            this@RestaurantRegistrationActivity, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            100
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED) {
                // User checks permission.
            } else {
                Toast.makeText(this@RestaurantRegistrationActivity, "Permission is denied.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    override fun onSuccess(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onFailed(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onOwnerBasicDetailFound() {
       restaurantViewModel.getAllRequiredRestaurantObservable()
           .observe(this, Observer {
               if (it != null && it.result) {
                   showRegistrationFormToUser(it)
               }
               else onFailed("Basic Details Not Found")
           })
    }

    private fun showRegistrationFormToUser(it: GetAllRestaurantDocsResponse) {

        var ownerDocsList = ArrayList<RestaurantOwnerRequiredDoc>()
        var mainList = ArrayList<RequireRestaurantDocs>()

        mainList = it.requiredDocs as ArrayList<RequireRestaurantDocs>

        for (count in mainList.indices){
            ownerDocsList = mainList[count].restaurant_owner_required_docs as ArrayList<RestaurantOwnerRequiredDoc>
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        restaurantDetailsAdapter = RestaurantDetailsAdapter(this)
        restaurantDetailsAdapter.addOwnerDetails(ownerDocsList)
        binding.recyclerView.adapter = restaurantDetailsAdapter

        restaurantDetailsAdapter.notifyDataSetChanged()

        binding.btnSaveConfirm.setOnClickListener {
            for (count in restaurantDetailsAdapter.ownerRequiredDoc.indices){
                val editVal : String = restaurantDetailsAdapter.ownerRequiredDoc[count].editText
                var title : String = restaurantDetailsAdapter.ownerRequiredDoc[count].required_docs_name


                title = title.replace(" ".toRegex(), "_").toLowerCase()


                if (editVal!=null && editVal.isEmpty()){
                    onFailed("Please enter all information all are required")
                }

//                val jsonObject = JsonObject()
                jsonObject.addProperty(title, editVal)


            }

            jsonObject.addProperty("request_token",Helper.getAuthToken.authToken(this))
            Log.d("adapterText", "showRegistrationFormToUser: $jsonObject")

            restaurantViewModel._addRestaurantOwnerDetails(jsonObject)
        }

        restaurantViewModel.dismissProgressDai()
    }


    override fun onAddRestaurantOwnerDetailsSuccess() {
        restaurantViewModel.getRestaurantOwnerDetailsObservable()
                .observe(this, Observer {
                    if (it != null) {
                        if (it.result){
                            restaurantViewModel.dismissProgressDai()
                            onSuccess(it.message)
                            restaurantViewModel.trackRegistrationProcess()
                        }
                        else{
                            restaurantViewModel.dismissProgressDai()
                            onFailed(it.message)
                        }

                    }
                })
    }


    override fun toTrackRegistrationProcessSuccess() {
       restaurantViewModel.trackRegistrationFormObservable()
           .observe(this, Observer {
               if (it!=null){
                   Log.d("process", "toTrackRegistrationProcessSuccess: ${it.processCompleteStatus}")
                   restaurantViewModel.processCompleteStatusCode.set(it.processCompleteStatus)
                   showPendingRegistrationProcess(it)
                   restaurantViewModel.dismissProgressDai()

                   if (isShowSnackBar) parent_layout.snackbar("Please upload a next document..")
               }
               else {
                   onFailed("Information not found")
                   restaurantViewModel.dismissProgressDai()
               }
           })
    }

    //check first user registration process and show a form depends on api
    @SuppressLint("NotifyDataSetChanged")
    private fun showPendingRegistrationProcess(it: TrackRegistrationProcessResponse) {

        setRegistrationTitle(it)

        //storing list
        mainNeedToProcessCompleteList = it.needToProcessComplete as ArrayList<NeedToProcessComplete>

        //check list is not null
        if (mainNeedToProcessCompleteList != null && mainNeedToProcessCompleteList.isNotEmpty()) {

            for (count in mainNeedToProcessCompleteList.indices) {
                docsType = mainNeedToProcessCompleteList[count].required_docs_type
            }

            if (docsType == "image") {

                val numberOfColumns = 2
                binding.recyclerView.layoutManager = GridLayoutManager(this, numberOfColumns)

                binding.btnSaveConfirm.visibility = View.GONE

                //initialize adapter
                restaurantDetailsAdapter = RestaurantDetailsAdapter(this)
                restaurantDetailsAdapter.addRestaurantImages(mainNeedToProcessCompleteList)
                binding.recyclerView.adapter = restaurantDetailsAdapter
                restaurantDetailsAdapter.notifyDataSetChanged()

                restaurantDetailsAdapter.setOnItemClickListener(this)


            }
            else if (docsType == "text") {

                binding.btnSaveConfirm.visibility = View.VISIBLE
                binding.recyclerView.layoutManager = LinearLayoutManager(this)

                //initialize adapter
                restaurantDetailsAdapter = RestaurantDetailsAdapter(this)
                restaurantDetailsAdapter.trackRegistrationProcess(mainNeedToProcessCompleteList)
                binding.recyclerView.adapter = restaurantDetailsAdapter
                restaurantDetailsAdapter.notifyDataSetChanged()

                submitTheInfo(restaurantDetailsAdapter)
            }
            else if (docsType == "time"){

                binding.btnSaveConfirm.visibility = View.VISIBLE
                binding.recyclerView.layoutManager = LinearLayoutManager(this)

                //initialize adapter
                restaurantDetailsAdapter = RestaurantDetailsAdapter(this)
                restaurantDetailsAdapter.trackRegistrationProcess(mainNeedToProcessCompleteList)
                binding.recyclerView.adapter = restaurantDetailsAdapter
                restaurantDetailsAdapter.notifyDataSetChanged()

                submitTheInfo(restaurantDetailsAdapter)
            }

        } else if (it.processCompleteStatus == "4") {
            restaurantViewModel.moveOnRestaurantHome()
        } else {
            onFailed("Internal Server Error!!!")
        }
    }

    private fun submitTheInfo(restaurantDetailsAdapter: RestaurantDetailsAdapter) {

        binding.btnSaveConfirm.setOnClickListener {

            for (count in restaurantDetailsAdapter.needToProcessComplete.indices) {

                var title: String =
                    restaurantDetailsAdapter.needToProcessComplete[count].required_docs_name
                val editVal: String =
                    restaurantDetailsAdapter.needToProcessComplete[count].editText

                title = title.replace(" ".toRegex(), "_").toLowerCase()

                jsonObject.addProperty(title, editVal)

            }

            //adding request token in json object
            jsonObject.addProperty("request_token", Helper.getAuthToken.authToken(this))

            restaurantViewModel.checkProcessCompleteStatus(jsonObject)
        }
    }

    //set registration title on toolbar
    private fun setRegistrationTitle(it: TrackRegistrationProcessResponse) {
        if (it.processCompleteStatus!=null){
            when(it.processCompleteStatus){

                "0" -> {
                    binding.toolbar.title = "Owner Details"
                }
                "1" -> {
                    binding.toolbar.title = "Restaurant Details"
                }
                "2" -> {
                    binding.toolbar.title = "Restaurant Profile"
                }
                else -> {
                    binding.toolbar.title = "Restaurant Registration"
                }
            }
        }
    }


    override fun onAddRestaurantProfileDetailsSuccess() {
        restaurantViewModel.addRestaurantProfileDetailsObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        restaurantViewModel.dismissProgressDai()
                        onSuccess(it.message)
                        restaurantViewModel.trackRegistrationProcess()
                    }

                }
            })
    }

    override fun onAddRestaurantPhotoSuccess() {
        GlobalScope.launch(Dispatchers.Main){
            restaurantViewModel.addRestaurantPhotoObservable()
                .observe(this@RestaurantRegistrationActivity, Observer {
                    if (it!=null ){
                        if (it.result){
                            restaurantViewModel.dismissProgressDai()
                            onSuccess(it.message)
                            restaurantViewModel.trackRegistrationProcess()
                            isShowSnackBar = true
                        }
                        else{
                            restaurantViewModel.dismissProgressDai()
                            onFailed("Please try again the image is not uploaded")
                        }
                    }
                })
        }
    }


    //adapter click listener
    override fun onItemClick(position: Int) {

        imagePosition = position

        var title : String = mainNeedToProcessCompleteList[position].required_docs_name
        title = title.replace(" ".toRegex(), "_").toLowerCase()

        docsTitle.set(title)


        openImageChooser(position)
    }


    private fun openImageChooser(position: Int) {

        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"

            val mimeTypes = arrayOf("image/jpeg","image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)

            startActivityForResult(it, REQUEST_CODE_IMAGE_PICKER)
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
            onFailed("Please select the image")
        }
        else{

            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

//
//            val fullSizeBitmap : Bitmap = BitmapFactory.decodeFile(file.path)
//
//            val reduceBitmap : Bitmap? = ImageResizer.reduce.reduceBitmapSize(fullSizeBitmap,2048)
//
//            val reduceFile : File = getBitmapFile(reduceBitmap)

            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(this@RestaurantRegistrationActivity, file) {
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

                // MultipartBody.Part is used to send also the actual file name
                val body = MultipartBody.Part.createFormData("image", compressedImageFile.name, requestFile)


                val name: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    docsTitle.get()
                        .toString()
                )

                val requestToken: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    Helper.getAuthToken.authToken(this@RestaurantRegistrationActivity).toString()
                )

                // Log.d("CropImageChecker", "uploadImage: $body$name$requestToken")

                restaurantViewModel._addRestaurantPhoto(body,name,requestToken)
            }

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