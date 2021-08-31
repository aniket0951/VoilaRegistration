package com.voila.voilasailor.restaurantRegistration.restaurantViewModel

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.Helper.NetworkStatus
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.Adpter.FilterOptionAdapter
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.FilterOption
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.*
import com.voila.voilasailor.restaurantRegistration.RestaurantRespository.RestaurantHomeRepository
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.RestaurantHomeListener
import com.voila.voilasailor.restaurantRegistration.UI.MenuCardActivity
import kotlinx.android.synthetic.main.fileter_option_bottomsheet_layout.*

class RestaurantHomeViewModel(var context:Context) : ViewModel() {

    private lateinit var progressDialog: ProgressDialog
    var restaurantHomeRepository : RestaurantHomeRepository = RestaurantHomeRepository()
    lateinit var listener : RestaurantHomeListener
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var adapter : FilterOptionAdapter
    var jsonObject : JsonObject = JsonObject()

    //to check is account verify
    fun isAccountVerifyOrNot(request_token: String){
        progressDialog = Helper.DialogsUtils.showProgressDialog(context,"Please wait we are check is account verify or not...")
        isAccountVerify(request_token)
        listener.onIsAccountVerify()
    }

    //dismiss progress dai
    fun dismissProgressDai(){
        if (progressDialog!= null) progressDialog.dismiss()
    }

    fun addItemOnClick(){
        val intent = Intent(context,MenuCardActivity::class.java)
        context.startActivity(intent)
    }


    //get all menu
    fun getAllMenu(restaurant_token_id: String,restaurant_id: String){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDialog = Helper.DialogsUtils.showProgressDialog(
                context,
                "Please wait we are getting all menus"
            )
            getAllMenus(restaurant_id, restaurant_token_id)
            listener.onGetAllMenus()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    fun getAllFilterOption(){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDialog =
                Helper.DialogsUtils.showProgressDialog(context, "wait getting filter options")
            getAllFilterOptions()
            listener.onGetFilterOptions()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }

    //show a filter option
    fun showFilterOptions(getAllFilterOptionResponse: GetAllFilterOptionResponse) {

        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.fileter_option_bottomsheet_layout)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(true)

        bottomSheetDialog.filter_recycler_view.layoutManager = LinearLayoutManager(context)

        //get filter options
        var list : ArrayList<FilterOption> = ArrayList()

        list = getAllFilterOptionResponse.filterOptions as ArrayList<FilterOption>

        adapter = FilterOptionAdapter(context)
        adapter.addFilterOptions(list)

        bottomSheetDialog.filter_recycler_view.adapter = adapter

        dismissProgressDai()

        //select a filter
        selectFilter(adapter,list)

        bottomSheetDialog.show()
    }

    //dismiss bottom sheet dialog
    fun bottomSheetDismiss(){
        if (bottomSheetDialog!=null) bottomSheetDialog.dismiss()
    }

    private fun selectFilter(adapter: FilterOptionAdapter, list: ArrayList<FilterOption>) {
        adapter.setOnItemClickListener(object : FilterOptionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                progressDialog = Helper.DialogsUtils.showProgressDialog(context,"applying filter please wait..")

                val filter_type = list[position].filter_type

                Log.d("filterType", "onItemClick: $filter_type")

                jsonObject.addProperty("restaurant_token_id",Helper.getAuthToken.authToken(context))
                jsonObject.addProperty("filter_type",filter_type)

                getAllDishWithFilterOption(jsonObject)
                listener.onFilterApplySuccessfully()
            }
        })
    }

    //remove the dish
    fun _dishRemove(jsonObject: JsonObject){
        if (NetworkStatus.getInstance(context)?.isOnline()!!) {
            progressDialog =
                Helper.DialogsUtils.showProgressDialog(context, "Please wait dish is deleting...")
            dishRemove(jsonObject)
            listener.onRemoveTheDishSuccess()
        }
        else{
            Helper.OnInternetConnectionFailMSG.NetworkError(context as Activity)
        }
    }


    /*-------------------------------------- IN REPOSITORY -------------------------*/
    private val isAccountVerifyLiveData : MutableLiveData<IsAccountVerifyResponse> = restaurantHomeRepository.isAccountVerifyObservable()
    private val getAllMenusLiveData : MutableLiveData<GetMenusResponse> = restaurantHomeRepository.getMenusObservable()
    private val getAllFilterOptionLiveData : MutableLiveData<GetAllFilterOptionResponse> = restaurantHomeRepository.getAllFilterOptionsObservable()
    private val getAllDishWithFilterOptionLiveData : MutableLiveData<GetAllDishWithFilterOptionResponse> = restaurantHomeRepository.getAllDishWithFilterOptionObservable()
    private val dishRemoveLiveData : MutableLiveData<DishRemoveResponse> = restaurantHomeRepository.dishRemoveObservable()


    /*------------------------------------ CALL FROM REPO-----------------*/

    //is account verify
    private fun isAccountVerify(request_token:String){
        restaurantHomeRepository.isAccountVerify(request_token)
    }

    //get all menu
    private fun getAllMenus(restaurant_id:String,restaurant_token_id:String){
        restaurantHomeRepository.getAllMenus(restaurant_token_id, restaurant_id)
    }

    //get all filter options
    private fun getAllFilterOptions(){
        restaurantHomeRepository.getAllFilterOptions()
    }

    //after applying filter get all dish with filter
    fun getAllDishWithFilterOption(jsonObject: JsonObject){
        restaurantHomeRepository.getAllDishWithFilterOption(jsonObject)
    }

    //remove the dish
    private fun dishRemove(jsonObject: JsonObject){
        restaurantHomeRepository.dishRemove(jsonObject)
    }



    /*-----------------------------------  CALL OBSERVABLE -----------------------*/

    fun isAccountVerifyObservable() : MutableLiveData<IsAccountVerifyResponse>{
        return isAccountVerifyLiveData
    }

    fun getAllMenuObservable(): MutableLiveData<GetMenusResponse>{
        return getAllMenusLiveData
    }

    fun getAllFilterOptionObservable() : MutableLiveData<GetAllFilterOptionResponse>{
        return getAllFilterOptionLiveData
    }

    fun getAllDishWithFilterOptionObservable() : MutableLiveData<GetAllDishWithFilterOptionResponse>{
        return getAllDishWithFilterOptionLiveData
    }

    fun dishRemoveObservable():MutableLiveData<DishRemoveResponse>{
        return dishRemoveLiveData
    }


}