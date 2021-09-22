package com.voila.voilasailor



import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.voila.voilasailor.Adapter.RequireRestaurantDocsAdapter
import com.voila.voilasailor.Adapter.RequiredDocsAdapter
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.MainViewModelListener.MainViewModelListener
import com.voila.voilasailor.Model.*
import com.voila.voilasailor.NetworkResponse.GetAllRequiredDocsResponse
import com.voila.voilasailor.NetworkResponse.GetAllRestaurantDocsResponse
import com.voila.voilasailor.databinding.ActivityMainBinding
import com.voila.voilasailor.loginModule.LoginActivity
import com.voila.voilasailor.restaurantRegistration.Util.toasts
import com.voila.voilasailor.viewModel.MainActivityViewModel
import com.voila.voilasailor.viewModel.MainViewModelFactory
import kotlinx.android.synthetic.main.required_docs_bottomsheet.*

class MainActivity : AppCompatActivity() , MainViewModelListener {

    lateinit var activityMainBinding: ActivityMainBinding

    lateinit var requiredDocsAdapter: RequiredDocsAdapter
    lateinit var requireRestaurantDocsAdapter: RequireRestaurantDocsAdapter
    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var bottomSheetDialog : BottomSheetDialog

    private var isRestaurant : Boolean = false
    private var isDriver : Boolean = false
    var isRestaurantBottom : Boolean = false
    var isDriverBottom : Boolean = false
    private val REQUEST_APP_SETTINGS = 168

    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityViewModel = ViewModelProviders.of(this,MainViewModelFactory(this)).get(MainActivityViewModel::class.java)
        activityMainBinding.main = mainActivityViewModel
        mainActivityViewModel.listener = this
        mainActivityViewModel.checkUserLogin()
        mainActivityViewModel.isNetworkStateCheck()
        activityMainBinding.executePendingBindings()


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            ActivityCompat.requestPermissions(
//                this, arrayOf(
//                    Manifest.permission.RECEIVE_SMS,
//                    Manifest.permission.READ_SMS
//                ),
//                100)
//        }

        activityMainBinding.recyclerView.layoutManager = LinearLayoutManager(this)

    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun hasPermissions(@NonNull vararg permissions: String?): Boolean {
        for (permission in permissions) if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(
                permission!!
            )
        ) return false
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_APP_SETTINGS) {
            if (hasPermissions(*requiredPermissions)) {
                Toast.makeText(this, "All permissions granted!", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data)


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_APP_SETTINGS -> if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED) {
                // User checks permission.
                mainActivityViewModel.checkUserLogin()
            }
            else {
                toasts("permission denied please try again")
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        if (hasPermissions(*requiredPermissions)){
            if (isRestaurant && !isRestaurantBottom){
                mainActivityViewModel.registerAsRestaurant()
            }
            if (isDriver && !isDriverBottom){
                mainActivityViewModel.registerAsDriver()
            }
        }
    }

    override fun onDriverRequiredDocs() {
        activityMainBinding.progressBarBar.visibility = View.VISIBLE
        mainActivityViewModel.getAppRequiredDocsObservable()
                .observe(this, Observer {
                    if (it != null) {
                        showDocsRequiredList(it)
                        mainActivityViewModel.getAppRequiredDocsObservable()
                            .removeObservers(this)
                    }
                    else{
                        toasts("Please try again and check your internet connection")
                    }
                })
    }

    private fun showDocsRequiredList(getAllRequiredDocsResponse: GetAllRequiredDocsResponse) {

        activityMainBinding.progressBarBar.visibility = View.GONE


        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
        bottomSheetDialog.setContentView(R.layout.required_docs_bottomsheet)
        bottomSheetDialog.setCanceledOnTouchOutside(false)
        bottomSheetDialog.setCancelable(true)

       //val  bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetDialog.container);
       // bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

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
          //  finishAffinity()
        }

        bottomSheetDialog.next.setOnClickListener {
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            intent.putExtra("registrationFor",mainActivityViewModel.registrationFor.get())
            startActivity(intent)
           // finishAffinity()
        }
        bottomSheetDialog.show()
    }

    override fun onRestaurantRequiredDocs() {
        activityMainBinding.progressBarBar.visibility = View.VISIBLE
        mainActivityViewModel.getAllRequiredRestaurantObservable()
                .observe(this, Observer {
                    if (it != null) {
                        showRestaurantDocsList(it)
                        mainActivityViewModel.getAllRequiredRestaurantObservable()
                            .removeObservers(this)
                    }
                })
    }

    private fun showRestaurantDocsList(getAllRestaurantDocsResponse: GetAllRestaurantDocsResponse) {

        activityMainBinding.progressBarBar.visibility = View.GONE

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
           // finishAffinity()
        }

        bottomSheetDialog.next.setOnClickListener {
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            intent.putExtra("registrationFor",mainActivityViewModel.registrationFor.get())
            startActivity(intent)
          //  finishAffinity()
        }

        bottomSheetDialog.show()
    }

}



