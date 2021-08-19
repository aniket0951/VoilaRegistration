package com.example.voilaregistration.restaurantRegistration.UI

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.voilaregistration.Helper.Helper
import com.example.voilaregistration.R
import com.example.voilaregistration.databinding.ActivityRestaurantHomeScreenBinding
import com.example.voilaregistration.restaurantRegistration.Adpter.ApplyFilterOptionAdapter
import com.example.voilaregistration.restaurantRegistration.Adpter.GetAllMenusAdapter
import com.example.voilaregistration.restaurantRegistration.RestaurantFactory.RestaurantHomeViewModelFactory
import com.example.voilaregistration.restaurantRegistration.RestaurantModel.FilterDish
import com.example.voilaregistration.restaurantRegistration.RestaurantModel.Menu
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.GetAllDishWithFilterOptionResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.GetMenusResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantNetworkResponse.IsAccountVerifyResponse
import com.example.voilaregistration.restaurantRegistration.RestaurantViewModelListner.RestaurantHomeListener
import com.example.voilaregistration.restaurantRegistration.Util.authToken
import com.example.voilaregistration.restaurantRegistration.Util.toast
import com.example.voilaregistration.restaurantRegistration.restaurantViewModel.RestaurantHomeViewModel
import kotlinx.android.synthetic.main.activity_restaurant_home_screen.*


class RestaurantHomeScreenActivity : AppCompatActivity(), RestaurantHomeListener {

    private lateinit var restaurantHomeViewModel: RestaurantHomeViewModel
    private lateinit var binding: ActivityRestaurantHomeScreenBinding

    lateinit var adapter : GetAllMenusAdapter
    lateinit var filterDishAdapter: ApplyFilterOptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_restaurant_home_screen)
        restaurantHomeViewModel = ViewModelProviders.of(this,RestaurantHomeViewModelFactory(this)).get(RestaurantHomeViewModel::class.java)
        binding.restaurantHome = restaurantHomeViewModel
        restaurantHomeViewModel.listener = this

        this.authToken()?.let { restaurantHomeViewModel.isAccountVerifyOrNot(it) }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
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
                        onFailed(it.message)
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

    private fun showFilterDish(it: GetAllDishWithFilterOptionResponse) {
        var list : ArrayList<FilterDish> = ArrayList()

        list = it.filterDish as ArrayList<FilterDish>

        filterDishAdapter = ApplyFilterOptionAdapter(this)

        filterDishAdapter.getFilterDish(list)

        binding.recyclerView.adapter = filterDishAdapter

        restaurantHomeViewModel.dismissProgressDai()
        restaurantHomeViewModel.bottomSheetDismiss()
        onSuccess(it.message)
    }

    private fun showAllMenus(it: GetMenusResponse) {
        var list : ArrayList<Menu> = ArrayList()

        list = it.menus as ArrayList<Menu>

        adapter = GetAllMenusAdapter(this)
        adapter.getAllMenuList(list)

        binding.recyclerView.adapter = adapter
        binding.recyclerViewLayout.visibility = View.VISIBLE

        restaurantHomeViewModel.dismissProgressDai()
    }

    private fun isAccountUnderReview(it: IsAccountVerifyResponse) {
        if (it.isVerify){

            Glide.with(this)
                .load(it.verificationData)
                .into(account_under_review_img)

            under_review_text.text = it.message

            account_under_review_img.visibility = View.VISIBLE
            under_review_text.visibility = View.VISIBLE

            restaurantHomeViewModel.dismissProgressDai()
        }
        else if (!it.isVerify) {
            restaurantHomeViewModel.dismissProgressDai()
            restaurantHomeViewModel.getAllMenu(Helper.getAuthToken.authToken(this),Helper.getRestaurantId.restaurantId(this))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}