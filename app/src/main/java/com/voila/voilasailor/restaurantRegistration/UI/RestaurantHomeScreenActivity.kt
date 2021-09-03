package com.voila.voilasailor.restaurantRegistration.UI

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.ActivityRestaurantHomeScreenBinding
import com.voila.voilasailor.restaurantRegistration.Adpter.ApplyFilterOptionAdapter
import com.voila.voilasailor.restaurantRegistration.Adpter.GetAllMenusAdapter
import com.voila.voilasailor.restaurantRegistration.RestaurantFactory.RestaurantHomeViewModelFactory
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.FilterDish
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.Menu
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.GetAllDishWithFilterOptionResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.GetMenusResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.IsAccountVerifyResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.RestaurantHomeListener
import com.voila.voilasailor.restaurantRegistration.Util.toast
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.RestaurantHomeViewModel


class RestaurantHomeScreenActivity : AppCompatActivity(), RestaurantHomeListener {

    private lateinit var restaurantHomeViewModel: RestaurantHomeViewModel
    private lateinit var binding: ActivityRestaurantHomeScreenBinding

    lateinit var adapter : GetAllMenusAdapter
    lateinit var filterDishAdapter: ApplyFilterOptionAdapter
    var positions : Int = 0

    var isProfileFragmentOpen : Boolean = false

    var jsonObject:JsonObject = JsonObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_home_screen)
        restaurantHomeViewModel = ViewModelProviders.of(this,RestaurantHomeViewModelFactory(this)).get(RestaurantHomeViewModel::class.java)
        binding.restaurantHome = restaurantHomeViewModel
        restaurantHomeViewModel.listener = this


        restaurantHomeViewModel.isAccountVerifyOrNot(Helper.getAuthToken.authToken(this))
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.profileText.setOnClickListener {

            val fm : FragmentManager = supportFragmentManager
            val myFragment : ProfileFragment = ProfileFragment()
            fm.beginTransaction().replace(R.id.fragment_view, myFragment).commit()

            isProfileFragmentOpen = true
            binding.fragmentView.visibility = View.VISIBLE
            binding.layoutAccountUnderReview.visibility = View.GONE
            binding.optionsParentLayout.visibility = View.GONE

            binding.toolbar.title = "Registration process"
            binding.imageView.visibility = View.VISIBLE

        }

        binding.imageView.setOnClickListener { backImageClickEvent() }

    }

    private fun backImageClickEvent() {
        if (isProfileFragmentOpen){
            val fragment = ProfileFragment()
            supportFragmentManager.beginTransaction().remove(fragment).commit()

            binding.fragmentView.visibility = View.GONE
            binding.layoutAccountUnderReview.visibility = View.VISIBLE
            binding.optionsParentLayout.visibility = View.VISIBLE

            binding.toolbar.title = "Menu Card"
            binding.imageView.visibility = View.GONE

        }

    }

    override fun onSuccess(s: String) {
        this.toast(s)
    }

    override fun onFailed(s: String) {
       this.toast(s)
    }

    override fun onIsAccountVerify() {
        restaurantHomeViewModel.isAccountVerifyObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        isAccountUnderReview(it)
                    }
                    else{
                        restaurantHomeViewModel.dismissProgressDai()
                        onFailed(it.message)
                    }
                }
            })
    }

    override fun onGetAllMenus() {
        restaurantHomeViewModel.getAllMenuObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        showAllMenus(it)
                    }
                    else{
                        restaurantHomeViewModel.dismissProgressDai()
                        onFailed("Add New dish...")
                    }
                }
            })
    }

    override fun onGetFilterOptions() {
        restaurantHomeViewModel.getAllFilterOptionObservable()
            .observe(this, Observer {
                if (it!=null){

                    if (it.result){
                        restaurantHomeViewModel.showFilterOptions(it)
                        restaurantHomeViewModel.getAllFilterOptionObservable().removeObservers(this)
                    }
                    else{
                        restaurantHomeViewModel.dismissProgressDai()
                        onFailed(it.message)
                    }
                }
                else{
                    restaurantHomeViewModel.dismissProgressDai()
                    onFailed("Network error..Please try again")
                }
            })
    }

    override fun onFilterApplySuccessfully() {
        restaurantHomeViewModel.getAllDishWithFilterOptionObservable()
            .observe(this, Observer {
                if (it!=null){

                    if (it.result){

                        showFilterDish(it)
                    }
                    else{
                        restaurantHomeViewModel.dismissProgressDai()
                        onFailed(it.message)
                    }
                }
                else{
                    restaurantHomeViewModel.dismissProgressDai()
                    onFailed("Filter not apply please try again..")
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRemoveTheDishSuccess() {
        restaurantHomeViewModel.dishRemoveObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        restaurantHomeViewModel.dismissProgressDai()
                        onSuccess(it.message)
                    }
                    else{
                        restaurantHomeViewModel.dismissProgressDai()
                        onFailed("Dish not deleted please try again..")
                    }
                }
            })
    }

    private fun showFilterDish(it: GetAllDishWithFilterOptionResponse) {
        var list : ArrayList<FilterDish> = ArrayList()

        list = it.filterDish as ArrayList<FilterDish>

        filterDishAdapter = ApplyFilterOptionAdapter(this)

        filterDishAdapter.getFilterDish(list)

        binding.recyclerView.adapter = filterDishAdapter

        restaurantHomeViewModel.dismissProgressDai()
        restaurantHomeViewModel.bottomSheetDismiss()

        filterDishAdapter.editMenuClickListener(object : ApplyFilterOptionAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                editMenuByFilerDetails(filterDishAdapter,position)
            }
        })

        filterDishAdapter.deleteMenuClickListener(object : ApplyFilterOptionAdapter.OnDeleteListener {
            override fun onDeleteClick(position: Int) {
                dishRemoveFromFilter(position,filterDishAdapter)
            }
        })

    }

    private fun showAllMenus(it: GetMenusResponse) {
        var list : ArrayList<Menu> = ArrayList()

        list = it.menus as ArrayList<Menu>

        adapter = GetAllMenusAdapter(this)
        adapter.getAllMenuList(list)

        binding.recyclerView.adapter = adapter
        binding.recyclerViewLayout.visibility = View.VISIBLE

        adapter.editMenuClickListener(object : GetAllMenusAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                editMenuDetails(adapter,position)
            }
        })

        adapter.deleteMenuClickListener(object : GetAllMenusAdapter.OnDeleteListener {
            override fun onDeleteClick(position: Int) {
                positions = position
               // adapter.removeItem(position)
                dishRemove(position,adapter)
            }
        })
        restaurantHomeViewModel.dismissProgressDai()
    }

    private fun dishRemove(position: Int, adapter: GetAllMenusAdapter) {

        val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
        builder1.setMessage("Are you sure,you want to delete  " + adapter.menuList[position].dish_name.toString() + " ? ")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Delete",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel()

                jsonObject.addProperty("restaurant_token_id",Helper.getAuthToken.authToken(this))
                jsonObject.addProperty("restaurant_id",Helper.getRestaurantId.restaurantId(this))
                jsonObject.addProperty("id", this.adapter.menuList[position].id.toString())

                restaurantHomeViewModel._dishRemove(jsonObject)
                this.adapter.removeItem(position)

            })

        builder1.setNegativeButton(
            "No",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel()
                val alertD : AlertDialog = builder1.create()
                alertD.dismiss()
            })

        val alert11: AlertDialog = builder1.create()
        alert11.show()

    }

    private fun dishRemoveFromFilter(position: Int, filterAdapter: ApplyFilterOptionAdapter) {

        val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
        builder1.setMessage("Are you sure,you want to delete  " + filterAdapter.filterDishList[position].dish_name.toString() + " ? ")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            "Delete",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel()

                jsonObject.addProperty("restaurant_token_id",Helper.getAuthToken.authToken(this))
                jsonObject.addProperty("restaurant_id",Helper.getRestaurantId.restaurantId(this))
                jsonObject.addProperty("id", filterAdapter.filterDishList[position].id.toString())

                restaurantHomeViewModel._dishRemove(jsonObject)
                filterAdapter.removeItem(position)
                removeFilterDishResponse()
            })

        builder1.setNegativeButton(
            "No",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel()
                val alertD : AlertDialog = builder1.create()
                alertD.dismiss()
            })

        val alert11: AlertDialog = builder1.create()
        alert11.show()

    }

    private fun removeFilterDishResponse() {
        restaurantHomeViewModel.dishRemoveObservable()
            .observe(this, Observer {
                if (it!=null){
                    if (it.result){
                        restaurantHomeViewModel.dismissProgressDai()
                        onSuccess(it.message)

                    }
                    else{
                        restaurantHomeViewModel.dismissProgressDai()
                        onFailed("Dish not deleted please try again..")
                    }
                }
            })
    }


    /*---- update the menu details ----*/
    private fun editMenuDetails(adapter: GetAllMenusAdapter, position: Int) {
        val intent = Intent(this,UpdateMenuActivity::class.java)
        intent.putExtra("dishName",adapter.menuList[position].dish_name)
        intent.putExtra("dishDescription",adapter.menuList[position].dish_description)
        intent.putExtra("dishType",adapter.menuList[position].dish_type)
        intent.putExtra("dishPrice",adapter.menuList[position].dish_price)
        intent.putExtra("dishItem",adapter.menuList[position].dish_item)
        intent.putExtra("menuUrl",adapter.menuList[position].menuUrl)
        intent.putExtra("dishId",adapter.menuList[position].id.toString())

       // Log.d("dishId", "editMenuDetails: ${adapter.menuList[position].id}")
        startActivity(intent)

    }

    /*---- update filter menu details ---*/
    private fun editMenuByFilerDetails(adapter: ApplyFilterOptionAdapter, position: Int) {
        val intent = Intent(this,UpdateMenuActivity::class.java)
        intent.putExtra("dishName",adapter.filterDishList[position].dish_name)
        intent.putExtra("dishDescription",adapter.filterDishList[position].dish_description)
        intent.putExtra("dishType",adapter.filterDishList[position].dish_type)
        intent.putExtra("dishPrice",adapter.filterDishList[position].dish_price)
        intent.putExtra("dishItem",adapter.filterDishList[position].dish_item)
        intent.putExtra("menuUrl",adapter.filterDishList[position].menuUrl)
        intent.putExtra("dishId",adapter.filterDishList[position].id.toString())

      //  Log.d("dishId", "editMenuDetails: ${adapter.filterDishList[position].id}")
        startActivity(intent)

    }

    private fun isAccountUnderReview(it: IsAccountVerifyResponse) {

        if (!it.isVerify){

            Glide.with(this)
                .load(it.verificationData)
                .into(binding.accountUnderReviewImg)


            binding.underReviewText.text = it.message

            binding.accountUnderReviewImg.visibility = View.VISIBLE
            binding.underReviewText.visibility = View.VISIBLE

            binding.filterImg.visibility = View.GONE
            binding.profileText.visibility = View.VISIBLE

            binding.toolbar.title = "Account Under Review"

            restaurantHomeViewModel.dismissProgressDai()
        }
        else {
            restaurantHomeViewModel.dismissProgressDai()
            binding.filterImg.visibility = View.VISIBLE
            binding.profileText.visibility = View.GONE
            restaurantHomeViewModel.getAllMenu(Helper.getAuthToken.authToken(this),Helper.getRestaurantId.restaurantId(this))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
        finish()
    }
}