package com.voila.voilasailor.restaurantRegistration.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.theartofdev.edmodo.cropper.CropImage
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R.layout
import com.voila.voilasailor.databinding.ActivityMenuCardBinding
import com.voila.voilasailor.restaurantRegistration.Adpter.GetDishRequiredDocsAdapter
import com.voila.voilasailor.restaurantRegistration.RestaurantFactory.MenuCardViewModelFactory
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.GetAllRequiredDishDocsResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.ResultData
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.MenuCardViewListener
import com.voila.voilasailor.restaurantRegistration.Util.getFileName
import com.voila.voilasailor.restaurantRegistration.Util.toasts
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.MenuCardViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class MenuCardActivity : AppCompatActivity(),MenuCardViewListener,
    GetDishRequiredDocsAdapter.OnItemClickListener {

    private lateinit var menuCardViewModel : MenuCardViewModel
    lateinit var binding: ActivityMenuCardBinding

    private  var dishItem = ObservableField<String>()
    private  var docTitle = ObservableField<String>()
    private  var editVal = ObservableField<String>()
    private  var type : String = String()

    //get image path and adapter image position
    private var selectedImage : Uri? = null
    private var imagePosition : Int = 0

    //Assign the adapter
    lateinit var adapter : GetDishRequiredDocsAdapter

    var map:HashMap<String,RequestBody> = HashMap<String, RequestBody>()
    lateinit var body : MultipartBody.Part

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    var isImageSelected : Boolean = false

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layout.activity_menu_card)
        menuCardViewModel = ViewModelProviders.of(this,MenuCardViewModelFactory(this)).get(MenuCardViewModel::class.java)
        binding.menu = menuCardViewModel
        menuCardViewModel.listener = this
        binding.executePendingBindings()

        menuCardViewModel._getAllRequiredDishDocs()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        ActivityCompat.requestPermissions(
            this@MenuCardActivity, arrayOf(
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
                onFailed("Do not use camera....")
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED) {
                // User checks permission.
            } else {
                Toast.makeText(this@MenuCardActivity, "Permission is denied.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    override fun onSuccess(string: String) {
        this.toasts(string)
    }

    override fun onFailed(string: String) {
        this.toasts(string)
    }

    override fun onMoveHomeScreen() {
        val intent = Intent(this,RestaurantHomeScreenActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onGetRequiredDishDocsSuccess() {
        menuCardViewModel.getAllRequiredDishObservable()
            .observe(this, Observer {
                if (it!=null && it.result){
                    showDocsList(it)
                  //  onSuccess("Docs available")
                }
                else{
                    onFailed("No docs available..")
                }
            })
    }

    override fun onDishAddedSuccessfully() {
        menuCardViewModel.addNewDishObservable()
            .observe(this, Observer {
                if (it!=null){
                   menuCardViewModel.menuAddedSuccessfully(it)
                }
                else{
                    menuCardViewModel.dismissProgressDai()
                    onFailed("Internal Server Error...")
                }
            })
    }

    private fun showDocsList(it: GetAllRequiredDishDocsResponse) {
        var list = ArrayList<ResultData>()

        list = it.resultData as ArrayList<ResultData>
        adapter = GetDishRequiredDocsAdapter(this)
        adapter.addDishRequiredItems(list)
        binding.recyclerView.adapter = adapter

        binding.btnSaveConfirm.visibility = View.VISIBLE
        menuCardViewModel.dismissProgressDai()

        adapter.setOnItemClickListener(this)

        binding.btnSaveConfirm.setOnClickListener {

            getAllDishInfo(adapter)
           // menuCardViewModel.addNewDish(adapter)
        }
    }

    private fun getAllDishInfo(adapter: GetDishRequiredDocsAdapter) {

        for(count in adapter.dishRequiredItems.indices){

            if (adapter.dishRequiredItems[count].dishTypeSelected !=null && adapter.dishRequiredItems[count].dishTypeSelected.isNotEmpty()) {

                type = adapter.dishRequiredItems[count].dishTypeSelected
            }

            if (adapter.dishRequiredItems[count].autoCompleteTextView!=null && adapter.dishRequiredItems[count].autoCompleteTextView.isNotEmpty()) {

                dishItem.set(adapter.dishRequiredItems[count].autoCompleteTextView)
            }

            var title : String = adapter.dishRequiredItems[count].required_docs_name
            title = title.replace(" ".toRegex(), "_").toLowerCase()

            docTitle.set(title)
            editVal.set(adapter.dishRequiredItems[count].editText)

            map["dish_type"] = RequestBody.create("text/plain".toMediaTypeOrNull(), type)
            map["dish_item"] = RequestBody.create("text/plain".toMediaTypeOrNull(),dishItem.get().toString())
            map[docTitle.get().toString()] = RequestBody.create("text/plain".toMediaTypeOrNull(),editVal.get().toString())

        }

        map["restaurant_token_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(),Helper.getAuthToken.authToken(this))
        map["restaurant_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(),Helper.getRestaurantId.restaurantId(this))

        if (isImageSelected){
            menuCardViewModel._addNewDish(body,map)
        }
        else{
            onFailed("Please add image")
        }

    }

    /*--- on adapter image view click -----*/
    override fun onItemClick(position: Int) {

        imagePosition = position
        openImageChooser(position)
    }

    private fun openImageChooser(position: Int) {
        cropActivityResultLauncher.launch(null)

    }

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(16,9)
                .getIntent(this@MenuCardActivity)
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
            onFailed("Please select the image")
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
            //val file = File(cacheDir, contentResolver.getFileName(selectedImage!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

//            Log.d("fileLength", "uploadImage:before compress ${file.length()}")
//
//            val fullSizeBitmap : Bitmap = BitmapFactory.decodeFile(file.path)
//
//            val reduceBitmap : Bitmap? = ImageResizer.reduce.reduceBitmapSize(fullSizeBitmap,2048)
//
//           val reduceFile : File = getBitmapFile(reduceBitmap)

            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(this@MenuCardActivity, file) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(2_097_152) // 2 MB
                }
                //Log.d("fileLength", "uploadImage: after compress" + compressedImageFile.length())

                val requestFile = RequestBody.create(
                    contentResolver.getType(selectedImage!!)?.let { it.toMediaTypeOrNull() },
                    compressedImageFile
                )


                body = MultipartBody.Part.createFormData("image", compressedImageFile.name, requestFile)

            }

            adapter.isImageSelected(selectedImage!!)

            isImageSelected = true
        }
    }

    private fun getBitmapFile(reduceBitmap: Bitmap?): File {

        val file : File = File(cacheDir, selectedImage?.let { contentResolver.getFileName(it) })
        file.createNewFile()
        val bos : ByteArrayOutputStream = ByteArrayOutputStream()
        reduceBitmap?.compress(Bitmap.CompressFormat.JPEG,60,bos)
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