package com.example.voilaregistration.restaurantRegistration

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.Model.RequireRestaurantDocs
import com.example.voilaregistration.Model.RestaurantOwnerRequiredDoc
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import com.example.voilaregistration.R
import com.example.voilaregistration.databinding.ActivityRestaurantRegistrationBinding
import com.example.voilaregistration.restaurantRegistration.Adpter.RestaurantDetailsAdapter
import com.example.voilaregistration.restaurantRegistration.RestaurantFactory.RestaurantViewModelFactory
import com.example.voilaregistration.restaurantRegistration.RestaurantModel.NeedToProcessComplete
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.TrackRegistrationProcessResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner.RestaurantViewModelListener
import com.example.voilaregistration.restaurantRegistration.Util.getFileName
import com.example.voilaregistration.restaurantRegistration.Util.snackbar
import com.example.voilaregistration.restaurantRegistration.restaurantViewModel.RestaurantRegistrationViewModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_restaurant_registration.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
                    if (it != null && it.result) {
                        restaurantViewModel.dismissProgressDai()
                        onSuccess(it.message)
                    }
                    else {
                        restaurantViewModel.dismissProgressDai()
                        onFailed(it.message)
                    }
                })
    }


    override fun toTrackRegistrationProcessSuccess() {
       restaurantViewModel.trackRegistrationFormObservable()
           .observe(this, Observer {
               if (it!=null && it.result){
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


                //initialize adapter
                restaurantDetailsAdapter = RestaurantDetailsAdapter(this)
                restaurantDetailsAdapter.addRestaurantImages(mainNeedToProcessCompleteList)
                binding.recyclerView.adapter = restaurantDetailsAdapter
                restaurantDetailsAdapter.notifyDataSetChanged()

                restaurantDetailsAdapter.setOnItemClickListener(this)

            } else if (docsType == "text") {

                binding.recyclerView.layoutManager = LinearLayoutManager(this)

                //initialize adapter
                restaurantDetailsAdapter = RestaurantDetailsAdapter(this)
                restaurantDetailsAdapter.trackRegistrationProcess(mainNeedToProcessCompleteList)
                binding.recyclerView.adapter = restaurantDetailsAdapter
                restaurantDetailsAdapter.notifyDataSetChanged()

                binding.btnSaveConfirm.setOnClickListener {

                    for (count in restaurantDetailsAdapter.needToProcessComplete.indices) {

                        var title: String =
                            restaurantDetailsAdapter.needToProcessComplete[count].required_docs_name
                        val editVal: String =
                            restaurantDetailsAdapter.needToProcessComplete[count].editText

                        title = title.replace(" ".toRegex(), "_").toLowerCase()

//                if (title!=null && editVal!=null){
//                    onFailed("Please Enter all Failed")
//                }

                        jsonObject.addProperty(title, editVal)

                    }

                    //adding request token in json object
                    jsonObject.addProperty("request_token", Helper.getAuthToken.authToken(this))

                    restaurantViewModel.checkProcessCompleteStatus(jsonObject)
                }
            }

        } else if (it.processCompleteStatus == "4") {
            restaurantViewModel.moveOnRestaurantHome()
        } else {
            onFailed("Internal Server Error!!!")
        }
    }


    override fun onAddRestaurantProfileDetailsSuccess() {
        restaurantViewModel.addRestaurantProfileDetailsObservable()
            .observe(this, Observer {
                if (it!=null && it.result){
                      restaurantViewModel.dismissProgressDai()
                    onSuccess(it.message)
                }
                else{
                    onFailed(it.message)
                }
            })
    }

    override fun onAddRestaurantPhotoSuccess() {
        restaurantViewModel.addRestaurantPhotoObservable()
            .observe(this, Observer {
                if (it!=null && it.result){
                    restaurantViewModel.dismissProgressDai()
                    onSuccess(it.message)
                    restaurantViewModel.trackRegistrationProcess()
                    isShowSnackBar = true
                }
                else{
                    restaurantViewModel.dismissProgressDai()
                    onFailed("please try again")
                }
            })
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



            // create RequestBody instance from file
            val requestFile = RequestBody.create(
                MediaType.parse(contentResolver.getType(selectedImage!!)),
                file
            )

            // MultipartBody.Part is used to send also the actual file name
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)


            val name: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                docsTitle.get()
                    .toString()
            )

            val requestToken: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                Helper.getAuthToken.authToken(this).toString()
            )

          // Log.d("CropImageChecker", "uploadImage: $body$name$requestToken")

            restaurantViewModel._addRestaurantPhoto(body,name,requestToken)
        }
    }

    companion object{

        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}