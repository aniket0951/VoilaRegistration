package com.voila.voilasailor.restaurantRegistration.UI

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.FragmentProfileBinding
import com.voila.voilasailor.restaurantRegistration.Adpter.ProfileDetailsAdapter
import com.voila.voilasailor.restaurantRegistration.RestaurantFactory.ProfileVewModelFactory
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.OwnerInfo
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.RequestedInfo
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.RestaurantInfo
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.RestaurantProfilePic
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.GetRestaurantRequestedInfoResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.ProfileListener
import com.voila.voilasailor.restaurantRegistration.Util.getFileName
import com.voila.voilasailor.restaurantRegistration.Util.toast
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.ProfileDetailViewModel
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


class ProfileFragment : Fragment(),ProfileListener {

    lateinit var binding : FragmentProfileBinding
    lateinit var profileModel : ProfileDetailViewModel

    lateinit var adapter: ProfileDetailsAdapter
    var jsonObject : JsonObject = JsonObject()

    private var selectedImage : Uri? = null
    val imageTitle = ObservableField<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)
        binding.profile = profileModel
        profileModel.listener = this
        binding.executePendingBindings()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.btnSaveConfirm.setOnClickListener { updateRequiredDetails() }

        ActivityCompat.requestPermissions(
            this.requireActivity(), arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            100)


            return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED) {
                // User checks permission.
            } else {
                Toast.makeText(this.requireActivity(), "Permission is denied.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun updateRequiredDetails() {
        if (profileModel.isOwnerDetailsShow){
            for (count in adapter.ownerDetailsList.indices){
                jsonObject.addProperty("owner_name",adapter.ownerDetailsList[count].ownerName)
                jsonObject.addProperty("owner_email",adapter.ownerDetailsList[count].ownerEmail)
                jsonObject.addProperty("owner_contact_no",adapter.ownerDetailsList[count].ownerContactNo)
                jsonObject.addProperty("owner_current_address",adapter.ownerDetailsList[count].ownerCurrentAddress)
                jsonObject.addProperty("owner_permanent_address",adapter.ownerDetailsList[count].ownerPermanentAddress)

            }
            jsonObject.addProperty("request_token",Helper.getAuthToken.authToken(this.requireContext()))

            profileModel.updateTheInformation(jsonObject)
        }
        else if (profileModel.isRestaurantDetailsShow){

            for (count in adapter.restaurantDetailsList.indices){
                jsonObject.addProperty("restaurant_name",adapter.restaurantDetailsList[count].restaurantName)
                jsonObject.addProperty("restaurant_email",adapter.restaurantDetailsList[count].restaurantEmail)
                jsonObject.addProperty("restaurant_contact_no",adapter.restaurantDetailsList[count].restaurantContactNo)
                jsonObject.addProperty("restaurant_opening_time",adapter.restaurantDetailsList[count].restaurantOpeningTime)
                jsonObject.addProperty("restaurant_close_time",adapter.restaurantDetailsList[count].restaurantClosingTime)
                jsonObject.addProperty("restaurant_website",adapter.restaurantDetailsList[count].restaurantWebsite)
                jsonObject.addProperty("restaurant_establishment_year",adapter.restaurantDetailsList[count].restaurantEstablishmentYear)
                jsonObject.addProperty("restaurant_cuisines_type",adapter.restaurantDetailsList[count].restaurantCuisinesType)
            }

            jsonObject.addProperty("request_token",Helper.getAuthToken.authToken(this.requireContext()))

            profileModel.updateTheInformation(jsonObject)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileModel = activity?.run {
            ViewModelProviders.of(this,ProfileVewModelFactory(requireContext()))[ProfileDetailViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onSuccess(s:String) {
        requireContext().toast(s)
    }

    override fun onOwnerDetailsSuccess() {
        profileModel.getRestaurantRequestedInfoObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result && profileModel.isOwnerDetailsShow){
                        showOwnerDetails(it)
                        profileModel.dismissDai()
                    }
                    else{
                        profileModel.dismissDai()
                        onFailed("Owner Details Not Found Please try again..")
                    }
                }
            })
    }

    private fun showOwnerDetails(it: GetRestaurantRequestedInfoResponse) {
        var list : ArrayList<OwnerInfo> = ArrayList()

        var mainList : ArrayList<RequestedInfo> = ArrayList()

        mainList = it.requestedInfo as ArrayList<RequestedInfo>

        for (lists in mainList.indices){
            list = mainList[lists].ownerInfo as ArrayList<OwnerInfo>
        }

        adapter = ProfileDetailsAdapter(this.requireContext())
        adapter.showOwnerDetails(list)

        binding.recyclerView.adapter = adapter

        binding.textParentLayout.visibility = View.GONE
        binding.recyclerViewLayout.visibility = View.VISIBLE
    }

    override fun onRestaurantDetailsSuccess() {
        profileModel.getRestaurantRequestedInfoObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result && profileModel.isRestaurantDetailsShow){
                        showRestaurantDetails(it)
                        profileModel.dismissDai()
                    }
                    else{
                        profileModel.dismissDai()
                        onFailed("Restaurant Details Not Found Please try again..")
                    }
                }
            })
    }

    private fun showRestaurantDetails(it: GetRestaurantRequestedInfoResponse) {
        var list : ArrayList<RestaurantInfo> = ArrayList()

        var mainList : ArrayList<RequestedInfo> = ArrayList()

        mainList = it.requestedInfo as ArrayList<RequestedInfo>

        for (lists in mainList.indices){
            list = mainList[lists].restaurantInfo as ArrayList<RestaurantInfo>
        }

        adapter = ProfileDetailsAdapter(this.requireContext())
        adapter.showRestaurantDetails(list)

        binding.recyclerView.adapter = adapter

        binding.textParentLayout.visibility = View.GONE
        binding.recyclerViewLayout.visibility = View.VISIBLE
    }

    override fun onRestaurantDocsDetailsSuccess() {
        profileModel.getRestaurantRequestedInfoObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result && profileModel.isRestaurantDocsShow){
                        showRestaurantDocsDetails(it)
                        profileModel.dismissDai()
                    }
                    else{
                        profileModel.dismissDai()
                        onFailed("Restaurant Document Details Not Found Please try again..")
                    }
                }
            })
    }

    override fun onOwnerDetailsUpdateSuccessfully() {
        profileModel.getRestaurantOwnerDetailsObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        profileModel.dismissDai()
                        onSuccess("Restaurant owner details update successfully...")
                        profileModel.onCancelClick()
                    }
                    else{
                        profileModel.dismissDai()
                        onFailed("Owner Details not updated please try again after some time..")
                        profileModel.onCancelClick()
                    }
                }
            })
    }

    override fun onRestaurantDetailsUpdateSuccessfully() {
       profileModel.addRestaurantProfileDetailsObservable()
           .observe(this, Observer {
               if (it!=null){
                   if (it.result){
                       profileModel.dismissDai()
                       onSuccess("Restaurant profile details update successfully")
                       profileModel.onCancelClick()
                   }
                   else{
                       profileModel.dismissDai()
                       onFailed("Restaurant Details not updated please try again after some time..")
                       profileModel.onCancelClick()
                   }
               }
           })
    }

    override fun onRestaurantDocumentUpdateSuccessfully() {
        GlobalScope.launch(Dispatchers.Main) {

            profileModel.addRestaurantPhotoObservable()
                .observe(requireActivity(), Observer {
                    if (it!=null){
                        if (it.result){
                            profileModel.dismissDai()
                            onSuccess(it.message)
                            profileModel.onCancelClick()
                        }
                        else{
                            profileModel.dismissDai()
                            onFailed("Restaurant Document not updated please try again after some time..")
                            profileModel.onCancelClick()
                        }
                    }
                })
        }
    }

    private fun showRestaurantDocsDetails(it: GetRestaurantRequestedInfoResponse) {
        var list : ArrayList<RestaurantProfilePic> = ArrayList()

        var mainList : ArrayList<RequestedInfo> = ArrayList()

        mainList = it.requestedInfo as ArrayList<RequestedInfo>

        for (lists in mainList.indices){
            list = mainList[lists].restaurantProfilePic as ArrayList<RestaurantProfilePic>
        }

        adapter = ProfileDetailsAdapter(this.requireContext())
        adapter.showRestaurantDocsDetails(list)

        binding.recyclerView.adapter = adapter

        adapter.restaurantOutdoorClickListener(object : ProfileDetailsAdapter.OnOutdoorClickListener {
            override fun onOutDoorItemClick(position: Int) {
                imageTitle.set("restaurant_outdoor_photo")
                openImageChooser(position)
            }
        })

        adapter.restaurantIndoorClickListener(object : ProfileDetailsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                imageTitle.set("restaurant_indoor_photo")
                openImageChooser(position)
            }
        })

        adapter.restaurantLicenceClickListener(object : ProfileDetailsAdapter.OnLicenceClickListener {
            override fun onLicenceItemClick(position: Int) {
                imageTitle.set("restaurant_licence_photo")
                openImageChooser(position)
            }
        })

        binding.textParentLayout.visibility = View.GONE
        binding.recyclerViewLayout.visibility = View.VISIBLE
    }

    override fun onFailed(s:String) {
        requireContext().toast(s)
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
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    private  fun uploadImage() {
        if (selectedImage==null){
            onFailed("Please select the image")
        }
        else{

            val parcelFileDescriptor =
                requireActivity().contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(requireActivity().cacheDir, requireActivity().contentResolver.getFileName(selectedImage!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

//            val fullSizeBitmap : Bitmap = BitmapFactory.decodeFile(file.path)
//
//            val reduceBitmap : Bitmap? = ImageResizer.reduce.reduceBitmapSize(fullSizeBitmap,2048)
//
//            val reduceFile : File = getBitmapFile(reduceBitmap)

            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(requireActivity(), file) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(2_097_152) // 2 MB
                }

                // create RequestBody instance from file
                val requestFile = RequestBody.create(
                    requireActivity().contentResolver.getType(selectedImage!!)?.let { it.toMediaTypeOrNull() },
                    compressedImageFile
                )

                // MultipartBody.Part is used to send also the actual file name
                val body = MultipartBody.Part.createFormData("image", compressedImageFile.name, requestFile)


                val name: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    imageTitle.get().toString()
                )

                val requestToken: RequestBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    Helper.getAuthToken.authToken(requireContext()).toString()
                )

                profileModel.updateRestaurantDocsImage(body,name,requestToken)
            }

           val progressDai : ProgressDialog = Helper.DialogsUtils.showProgressDialog(
                context,
                "Please wait we are updating restaurant document photo ..."
            )
        }
    }

    private fun getBitmapFile(reduceBitmap: Bitmap?): File {

        val file : File = File(requireActivity().cacheDir, selectedImage?.let { requireActivity().contentResolver.getFileName(it) })
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
}