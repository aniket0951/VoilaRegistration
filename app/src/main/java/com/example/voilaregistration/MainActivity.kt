package com.example.voilaregistration



import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voilaregistration.Adapter.RequireRestaurantDocsAdapter
import com.example.voilaregistration.Adapter.RequiredDocsAdapter
import com.example.voilaregistration.MainViewModelListener.MainViewModelListener
import com.example.voilaregistration.Model.*
import com.example.voilaregistration.NetworkResponse.GetAllRequiredDocsResponse
import com.example.voilaregistration.NetworkResponse.GetAllRestaurantDocsResponse
import com.example.voilaregistration.databinding.ActivityMainBinding
import com.example.voilaregistration.loginModule.LoginActivity
import com.example.voilaregistration.viewModel.MainActivityViewModel
import com.example.voilaregistration.viewModel.MainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.required_docs_bottomsheet.*

class MainActivity : AppCompatActivity() , MainViewModelListener {

    lateinit var activityMainBinding: ActivityMainBinding

    lateinit var requiredDocsAdapter: RequiredDocsAdapter
    lateinit var requireRestaurantDocsAdapter: RequireRestaurantDocsAdapter
    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var bottomSheetDialog : BottomSheetDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityViewModel = ViewModelProviders.of(this,MainViewModelFactory(this)).get(MainActivityViewModel::class.java)
        activityMainBinding.main = mainActivityViewModel
        mainActivityViewModel.listener = this
        mainActivityViewModel.checkUserLogin()
        activityMainBinding.executePendingBindings()

        activityMainBinding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onSuccess() {

    }

    override fun onFailed() {

    }

    override fun onDriverRequiredDocs() {
        mainActivityViewModel.getAppRequiredDocsObservable()
                .observe(this, Observer {
                    if (it != null) {
                        showDocsRequiredList(it)
                    }
                })
    }

    private fun showDocsRequiredList(getAllRequiredDocsResponse: GetAllRequiredDocsResponse) {

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
        bottomSheetDialog.setContentView(R.layout.required_docs_bottomsheet)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(true)


        bottomSheetDialog.recycler_view.layoutManager = LinearLayoutManager(this)

        var list = ArrayList<RequiredDoc>()
        var kycDetailsList = ArrayList<DriverKycRequiredDoc>()
        var vehicleRequiredDocList = ArrayList<DriverVehicleRequiredDoc>()

        list = getAllRequiredDocsResponse.requiredDocs as ArrayList<RequiredDoc>


        for (count in list.indices){
            if( list[count].driver_kyc_required_docs!=null && list[count].driver_vehicle_required_docs!=null){
                kycDetailsList = list[count].driver_kyc_required_docs as ArrayList<DriverKycRequiredDoc>
                vehicleRequiredDocList = list[count].driver_vehicle_required_docs as ArrayList<DriverVehicleRequiredDoc>
            }
        }

        requiredDocsAdapter = RequiredDocsAdapter(this)
        requiredDocsAdapter.getVehicle(vehicleRequiredDocList)
        requiredDocsAdapter.getKYCList(kycDetailsList)
        bottomSheetDialog.recycler_view.adapter = requiredDocsAdapter
        requiredDocsAdapter.notifyDataSetChanged()

        bottomSheetDialog.btn_conform_voilacab.setOnClickListener {
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            intent.putExtra("registrationFor",mainActivityViewModel.registrationFor.get())
            startActivity(intent)
        }

        bottomSheetDialog.show()
    }

    override fun onRestaurantRequiredDocs() {
        mainActivityViewModel.getAllRequiredRestaurantObservable()
                .observe(this, Observer {
                    if (it != null) {
                        showRestaurantDocsList(it)
                    }
                })
    }

    private fun showRestaurantDocsList(getAllRestaurantDocsResponse: GetAllRestaurantDocsResponse) {

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
        bottomSheetDialog.setContentView(R.layout.required_docs_bottomsheet)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(true)

        bottomSheetDialog.recycler_view.layoutManager = LinearLayoutManager(this)

        /*creating local list*/
        var list = ArrayList<RequireRestaurantDocs>()
        var restaurantDetailsRequiredDoc = ArrayList<RestaurantDetailsRequiredDoc>()
        var restaurantOwnerRequiredDoc = ArrayList<RestaurantOwnerRequiredDoc>()

        list = getAllRestaurantDocsResponse.requiredDocs as ArrayList<RequireRestaurantDocs>

        for (count in list.indices){
            if (list[count].restaurant_details_required_docs!=null && list[count].restaurant_owner_required_docs!=null){

                restaurantDetailsRequiredDoc = list[count].restaurant_details_required_docs as ArrayList<RestaurantDetailsRequiredDoc>
                restaurantOwnerRequiredDoc = list[count].restaurant_owner_required_docs as ArrayList<RestaurantOwnerRequiredDoc>
            }
        }

        /* Getting all objects from array */
        bottomSheetDialog.recycler_view.layoutManager = LinearLayoutManager(this)
        requireRestaurantDocsAdapter = RequireRestaurantDocsAdapter(this)
        requireRestaurantDocsAdapter.addRestaurantDetailsDocs(restaurantDetailsRequiredDoc)
        requireRestaurantDocsAdapter.addOwnerRequiredDocs(restaurantOwnerRequiredDoc)

        bottomSheetDialog.recycler_view.adapter =  requireRestaurantDocsAdapter
        requireRestaurantDocsAdapter.notifyDataSetChanged()

        bottomSheetDialog.btn_conform_voilacab.setOnClickListener {
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            intent.putExtra("registrationFor",mainActivityViewModel.registrationFor.get())
            startActivity(intent)
        }

        bottomSheetDialog.show()
    }

}



