package com.example.voilaregistration.restaurantRegistration.UI

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.R
import com.example.voilaregistration.databinding.ActivityMenuCardBinding
import com.example.voilaregistration.restaurantRegistration.Adpter.GetDishRequiredDocsAdapter
import com.example.voilaregistration.restaurantRegistration.RestaurantFactory.MenuCardViewModelFactory
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.GetAllRequiredDishDocsResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.ResultData
import com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner.MenuCardViewListener
import com.example.voilaregistration.restaurantRegistration.Util.getFileName
import com.example.voilaregistration.restaurantRegistration.Util.toast
import com.example.voilaregistration.restaurantRegistration.restaurantViewModel.MenuCardViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu_card)
        menuCardViewModel = ViewModelProviders.of(this,MenuCardViewModelFactory(this)).get(MenuCardViewModel::class.java)
        binding.menu = menuCardViewModel
        menuCardViewModel.listener = this
        binding.executePendingBindings()

        menuCardViewModel._getAllRequiredDishDocs()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onSuccess(string: String) {
        this.toast(string)
    }

    override fun onFailed(string: String) {
        this.toast(string)
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
                    onSuccess("Docs available")
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

            map["dish_type"] = RequestBody.create(  MediaType.parse("text/plain"), type)
            map["dish_item"] = RequestBody.create(MediaType.parse("text/plain"),dishItem.get().toString())
            map[docTitle.get().toString()] = RequestBody.create(MediaType.parse("text/plain"),editVal.get().toString())

           // Log.d("allAdapterValue", "getAllDishInfo from for loop: ${dishItem.get().toString()} + $type")
        }

        map["restaurant_token_id"] = RequestBody.create(MediaType.parse("text/plain"),Helper.getAuthToken.authToken(this))
        map["restaurant_id"] = RequestBody.create(MediaType.parse("text/plain"),Helper.getRestaurantId.restaurantId(this))

        menuCardViewModel._addNewDish(body,map)
    }

    /*--- on adapter image view click -----*/
    override fun onItemClick(position: Int) {

        imagePosition = position
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
             body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            adapter.isImageSelected(imagePosition)

        }
    }

    companion object{

        private const val REQUEST_CODE_IMAGE_PICKER = 100
    }

}