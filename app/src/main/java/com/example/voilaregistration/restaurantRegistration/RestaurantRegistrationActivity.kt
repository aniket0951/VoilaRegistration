package com.example.voilaregistration.restaurantRegistration

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.Model.RequireRestaurantDocs
import com.example.voilaregistration.Model.RestaurantOwnerRequiredDoc
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import com.example.voilaregistration.R
import com.example.voilaregistration.databinding.ActivityRestaurantRegistrationBinding
import com.example.voilaregistration.restaurantRegistration.Adpter.RestaurantDetailsAdapter
import com.example.voilaregistration.restaurantRegistration.RestaurantFactory.RestaurantViewModelFactory
import com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner.RestaurantViewModelListener
import com.example.voilaregistration.restaurantRegistration.restaurantViewModel.RestaurantRegistrationViewModel
import com.google.gson.JsonObject

class RestaurantRegistrationActivity : AppCompatActivity(),RestaurantViewModelListener {

    lateinit var restaurantViewModel  : RestaurantRegistrationViewModel
    lateinit var binding : ActivityRestaurantRegistrationBinding

    lateinit var progressDialog : ProgressDialog
    var jsonObject : JsonObject = JsonObject()

    //adapter
     lateinit var restaurantDetailsAdapter: RestaurantDetailsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_restaurant_registration)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_registration)
        restaurantViewModel = ViewModelProviders.of(this, RestaurantViewModelFactory(this)).get(RestaurantRegistrationViewModel::class.java)
        restaurantViewModel.listener = this
        binding.restaurant = restaurantViewModel
        binding.executePendingBindings()

        restaurantViewModel.showRegistrationForm()
    }

    override fun onOwnerBasicDetailFound() {
       restaurantViewModel.getAllRequiredRestaurantObservable()
           .observe(this, Observer {
               if (it != null && it.result) {
                   showRegistrationFormToUser(it)
               }
               else onOwnerBasicDetailsNotFound("Basic Details Not Found")
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
                    onAddRestaurantOwnerDetailsFailed("Please enter all information all are required")
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

    override fun onOwnerBasicDetailsNotFound(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

    override fun onAddRestaurantOwnerDetailsSuccess() {
        restaurantViewModel.getRestaurantOwnerDetailsObservable()
                .observe(this, Observer {
                    if (it != null && it.result) {
                        restaurantViewModel.dismissProgressDai()
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                    else {
                        restaurantViewModel.dismissProgressDai()
                        onAddRestaurantOwnerDetailsFailed(it.message)
                    }
                })
    }

    override fun onAddRestaurantOwnerDetailsFailed(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}