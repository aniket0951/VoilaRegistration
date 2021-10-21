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
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.theartofdev.edmodo.cropper.CropImage
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.ActivityUpdateMenuBinding
import com.voila.voilasailor.restaurantRegistration.RestaurantFactory.UpdateMenuViewModelFactory
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.UpdateMenuViewModelListener
import com.voila.voilasailor.restaurantRegistration.Util.getFileName
import com.voila.voilasailor.restaurantRegistration.Util.toasts
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.UpdateMenuViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.activity_update_menu.*
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

class UpdateMenuActivity : AppCompatActivity(), UpdateMenuViewModelListener {

    lateinit var  binding : ActivityUpdateMenuBinding
    private lateinit var updateMenuModel : UpdateMenuViewModel

    private lateinit var menuUrl:String
    private lateinit var dishTye:String
    lateinit var dishId:String

    private var selectedImage : Uri? = null
    var isNewImageSelected : Boolean = false

    private var map: HashMap<String, RequestBody> = HashMap<String, RequestBody>()
    private lateinit var body : MultipartBody.Part

    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    var jsonObject:JsonObject = JsonObject()


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_update_menu)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_menu)
        updateMenuModel = ViewModelProviders.of(this,UpdateMenuViewModelFactory(this)).get(UpdateMenuViewModel::class.java)
        binding.updatemodel = updateMenuModel
        updateMenuModel.listener = this
        binding.executePendingBindings()

        if (intent!=null){
            dishTye = intent.getStringExtra("dishType").toString()
            menuUrl = intent.getStringExtra("menuUrl").toString()
            dishId = intent.getStringExtra("dishId").toString()
            binding.dishNameEdit.setText(intent.getStringExtra("dishName"))
            binding.dishDescriptionEdit.setText(intent.getStringExtra("dishDescription"))
            binding.autoCompleteText.setText(intent.getStringExtra("dishItem"))
            binding.dishPriceEdit.setText(intent.getStringExtra("dishPrice"))

            updateMenuModel.menuUrl.set(menuUrl)
        }

        Glide.with(this)
            .load(menuUrl)
            .into(binding.dishImage)

        if (dishTye == "Veg"){
            binding.radioButton1.isChecked = true
            binding.radioButton2.isChecked = false
            updateMenuModel.dishType.set("Veg")
        }
        else{
            binding.radioButton2.isChecked = true
            binding.radioButton1.isChecked = false
            updateMenuModel.dishType.set("Non Veg")
        }

         val dishItems: Array<String> = this.resources.getStringArray(R.array.menuItems)
         val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_items,dishItems)

        binding.autoCompleteText.setAdapter(arrayAdapter)


        binding.dishImage.setOnClickListener { openImageChooser() }
        binding.btnSaveConfirm.setOnClickListener { updateDishInfo() }

        binding.radioButton1.setOnClickListener {
            binding.radioButton1.isChecked = true
            binding.radioButton2.isChecked = false
        }

        binding.radioButton2.setOnClickListener {
            binding.radioButton2.isChecked = true
            binding.radioButton1.isChecked = false
        }

        ActivityCompat.requestPermissions(
            this@UpdateMenuActivity, arrayOf(
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

        //get update result
        getMenuUpdateResult()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED) {
                // User checks permission.
            } else {
                Toast.makeText(this@UpdateMenuActivity, "Permission is denied.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }


    private fun getMenuUpdateResult() {
        updateMenuModel.updateMenuObservable()
            .observe(this, androidx.lifecycle.Observer {
                if (it!=null){
                    if (it.result){
                        onSuccess(it.message)
                        val intent = Intent(this,RestaurantHomeScreenActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        onFailed(it.message)
                    }
                }
            })
    }

    private fun openImageChooser() {

        cropActivityResultLauncher.launch(null)
    }

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(16,9)
                .getIntent(this@UpdateMenuActivity)
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
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

           // Log.d("fileLength", "uploadImage before: ${file.length()}")

//            val fullSizeBitmap : Bitmap = BitmapFactory.decodeFile(file.path)
//
//            val reduceBitmap : Bitmap? = ImageResizer.reduce.reduceBitmapSize(fullSizeBitmap,2048)
//
//            val reduceFile : File = getBitmapFile(reduceBitmap)
//
//            Log.d("fileLength", "uploadImage: ${reduceFile.length()}")

            GlobalScope.launch {
                val compressedImageFile = Compressor.compress(this@UpdateMenuActivity, file) {
                    resolution(1280, 720)
                    quality(80)
                    format(Bitmap.CompressFormat.JPEG)
                    size(2_097_152) // 2 MB
                }

               // Log.d("fileLength", "uploadImage after: ${compressedImageFile.length()}")

                // create RequestBody instance from file
                val requestFile = RequestBody.create(
                    contentResolver.getType(selectedImage!!)?.let { it.toMediaTypeOrNull() },
                    compressedImageFile
                )

                // MultipartBody.Part is used to send also the actual file name
                body = MultipartBody.Part.createFormData("image", compressedImageFile.name, requestFile)


                isNewImageSelected = true
            }

            binding.dishImage.setImageResource(R.drawable.ic_check)
        }
    }

    private fun getBitmapFile(reduceBitmap: Bitmap?): File {

        val file : File = File(cacheDir, selectedImage?.let { contentResolver.getFileName(it) })
        file.createNewFile()
        val bos : ByteArrayOutputStream = ByteArrayOutputStream()
        reduceBitmap?.compress(Bitmap.CompressFormat.JPEG,100,bos)
        val bitmapData : ByteArray = bos.toByteArray()

//        val bitmapImage = BitmapFactory.decodeFile("Your path")
//        val nh = (bitmapImage.height * (512.0 / bitmapImage.width)).toInt()
//        val scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true)
//
        val fos : FileOutputStream = FileOutputStream(file)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return file
    }

    companion object{

        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

    override fun onFailed(string: String) {
        toasts(string)
    }

    override fun onSuccess(string: String) {
        toasts(string)
    }

    override fun onMenuUpdateWithImage() {
        updateMenuModel.updateMenuWithImageObservable()
            .observe(this, androidx.lifecycle.Observer {
                if (it!=null){
                    if (it.result){
                        updateMenuModel.dismissDialog()
                        onSuccess(it.message)
                        updateMenuModel.moveOnHomeScreen()
                    }
                    else{
                        updateMenuModel.dismissDialog()
                        onFailed(it.message)
                        updateMenuModel.moveOnHomeScreen()
                    }
                }
            })
    }

    override fun onMenuUpdate() {
        updateMenuModel.updateMenuObservable()
            .observe(this, androidx.lifecycle.Observer {
                if (it!=null){

                    if (it.result){
                        updateMenuModel.dismissDialog()
                        onSuccess(it.message)
                        updateMenuModel.moveOnHomeScreen()
                    }
                    else{
                        updateMenuModel.dismissDialog()
                        onFailed(it.message)
                        updateMenuModel.moveOnHomeScreen()
                    }
                }
            })
    }



    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun updateDishInfo(){
        val dishName:String = dish_name_edit.text.toString().trim()
        val dishDescription = dish_description_edit.text.toString().trim()
        val dishPrice = dish_price_edit.text.toString().trim()
        val dishItem = auto_complete_text.text.toString().trim()
       var dishType:String = ""

        if (binding.radioButton1.isChecked){
            dishType = "Veg"
        }
        else if(binding.radioButton2.isChecked){
            dishType = "Non Veg"
        }

        map["dish_name"] = RequestBody.create("text/plain".toMediaTypeOrNull(), dishName)
        map["dish_description"] = RequestBody.create("text/plain".toMediaTypeOrNull(), dishDescription)
        map["dish_price"] = RequestBody.create("text/plain".toMediaTypeOrNull(), dishPrice)
        map["dish_type"] = RequestBody.create("text/plain".toMediaTypeOrNull(), dishType)
        map["dish_item"] = RequestBody.create("text/plain".toMediaTypeOrNull(), dishItem)
        map["restaurant_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), Helper.getRestaurantId.restaurantId(this))
        map["id"] = RequestBody.create("text/plain".toMediaTypeOrNull(),dishId)
        map["restaurant_token_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(),Helper.getAuthToken.authToken(this))

        jsonObject.addProperty("dish_name",dishName)
        jsonObject.addProperty("dish_description",dishDescription)
        jsonObject.addProperty("dish_price",dishPrice)
        jsonObject.addProperty("dish_type",dishType)
        jsonObject.addProperty("dish_item",dishItem)
        jsonObject.addProperty("restaurant_id",Helper.getRestaurantId.restaurantId(this))
        jsonObject.addProperty("id",dishId)
        jsonObject.addProperty("restaurant_token_id",Helper.getAuthToken.authToken(this))


        if (!isNewImageSelected){

            updateMenuModel._updateMenu(jsonObject)
        }
        else{
            updateMenuModel._updateMenuWithImage(body,map)
        }

    }

}