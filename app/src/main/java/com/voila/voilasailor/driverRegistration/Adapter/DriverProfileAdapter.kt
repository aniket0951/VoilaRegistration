package com.voila.voilasailor.driverRegistration.Adapter


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.voila.voilasailor.R
import com.voila.voilasailor.driverRegistration.DriverProfileActivity
import com.voila.voilasailor.driverRegistration.Model.*
import kotlinx.android.synthetic.main.driver_profile_details.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DriverProfileAdapter(var context: Context) : RecyclerView.Adapter<DriverProfileAdapter.ViewHolder>() {

    val basicInfo : ArrayList<BasicRequestedInfo> = ArrayList()
    val kycInfo : ArrayList<KycRequestedInfo> = ArrayList()
    val addressInfo : ArrayList<AddressRequestedInfo> = ArrayList()
    val vehicleInfo : ArrayList<VehicleRequestedInfo> = ArrayList()
    val vehicleProfilePicInfo : ArrayList<VehicleProfilePic> = ArrayList()


    private lateinit var setDateListener : DatePickerDialog.OnDateSetListener

    private var mListener: OnBasicUpdateClick? = null
    var addressListener: OnAddressUpdateClick? = null
    var vehicleInfoListener : OnVehicleUpdateClick? = null
    var vehicleFrontListener : OnVehicleFrontClick? = null
    var vehicleRCListener : OnVehicleListenerClick? = null
    var insuranceListener : OnInsuranceListenerClick? = null
    var permitListener : OnPermitListenerClick? = null
    var adharFrontListener : OnAdharFrontListenerClick? = null
    var adharBackListener : OnAdharBackListener? = null
    var licenceFrontClick : OnLiceneceFrontListener? = null
    var liceneceBackListener : OnLiceneceBackListener?=null

    interface OnLiceneceBackListener {
        fun onLicenceBackClick(position: Int)
    }
    fun setOnLicenceBackClick(listener : OnLiceneceBackListener){
        liceneceBackListener = listener
    }

    interface OnLiceneceFrontListener {
        fun onLicenceFrontClick(position: Int)
    }
    fun setOnLicenceFrontClick(listener : OnLiceneceFrontListener){
        licenceFrontClick = listener
    }

    interface OnAdharBackListener {
        fun onAdharBackListener(position: Int)
    }
    fun setOnAdharBackClick(listener: OnAdharBackListener){
        adharBackListener = listener
    }

    interface OnAdharFrontListenerClick {
        fun aadharClick(position: Int)
    }
    fun setOnAdharFrontClick(listener : OnAdharFrontListenerClick){
        adharFrontListener = listener
    }

    interface OnPermitListenerClick {
        fun onPermitClick(position: Int)
    }

    fun setPermitClick(listener: OnPermitListenerClick){
        permitListener = listener
    }

    interface OnInsuranceListenerClick {
        fun onVehicleInsuranceClick(position: Int)
    }
    fun setInsuranceClick(listener: OnInsuranceListenerClick){
        insuranceListener = listener
    }
    interface OnVehicleListenerClick {
        fun onVehicleRcClick(position: Int)
    }

    fun setOnVehicleRCClick(listener : OnVehicleListenerClick){
        vehicleRCListener = listener
    }

    interface OnVehicleFrontClick {
        fun onVehicleFrontClick(position: Int)
    }

    fun setOnVehicleFrontClick(listener: OnVehicleFrontClick){
        vehicleFrontListener = listener
    }

    interface OnVehicleUpdateClick {
        fun onVehicleClick(position: Int)
    }

    fun setOnVehicleClick(listener: OnVehicleUpdateClick){
        vehicleInfoListener = listener
    }

    interface OnAddressUpdateClick {
        fun onAddressClick(position: Int)
    }

    fun setOnAddressClick(listener : OnAddressUpdateClick){
        addressListener = listener
    }

    var isBasicInfoShow : Boolean = false
     var isAddressInfoShow : Boolean = false
     var isKYCInfoShow : Boolean = false
    var isVehicleInfoShow : Boolean = false
    var isVehicleProfileInfoShow : Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DriverProfileAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.driver_profile_details, parent, false))

    }

    override fun onBindViewHolder(holder: DriverProfileAdapter.ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return  when {
            isBasicInfoShow -> {
                basicInfo.size
               // Log.d("adapterS", "getItemCount: zero size ")

            }
            isAddressInfoShow -> {
                addressInfo.size
            }
            isKYCInfoShow -> {
                kycInfo.size
            }
            isVehicleInfoShow -> {
                vehicleInfo.size
            }
            isVehicleProfileInfoShow -> {
                vehicleProfilePicInfo.size
            }
            else -> {
                0
              //  Log.d("adapterS", "getItemCount: zero size ")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility", "WeekBasedYear", "UseCompatLoadingForDrawables"
    )
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val basicInfoParent : LinearLayoutCompat = itemView.basic_info_parent
        private val addressInfoParent : LinearLayoutCompat = itemView.address_info_parent
        private val kycInfoParent : LinearLayoutCompat = itemView.kyc_info_parent
        private val vehicleInfoParent : LinearLayoutCompat = itemView.vehicle_info_parent
        private val vehicleDocumentInfoParent : LinearLayoutCompat = itemView.vehicle_document_info_parent

        val fullNameEdit : AppCompatEditText = itemView.full_name_edt
        val emailEdit : AppCompatEditText = itemView.email_edt
        val contactNumberEdit : AppCompatEditText = itemView.contact_number_edt
        val dateOfBirthEdit : AppCompatEditText = itemView.date_of_birth_edt

        val houseNumberEdit : AppCompatEditText = itemView.house_number_edt
        val buildingNameEdit : AppCompatEditText = itemView.building_name_edt
        val streetNameEdit : AppCompatEditText = itemView.street_name_edt
        val landMarkEdit : AppCompatEditText = itemView.landmark_edt
        val state : AutoCompleteTextView = itemView.state_edt
        val districtNameEdit : AutoCompleteTextView = itemView.district_edt
        val pinCodeEdit : AppCompatEditText = itemView.pin_code_edt

        private val adharFront : AppCompatImageView = itemView.aadhar_front_photo
        private val adharBack : AppCompatImageView = itemView.aadhar_back_photo
        private val licenceFront : AppCompatImageView = itemView.licence_front_photo
        private val licenceBack : AppCompatImageView = itemView.licence_back_photo
        private val txt_adhar_front : AppCompatTextView = itemView.txt_aadhar_front_photo
        private val txt_adhar_back : AppCompatTextView = itemView.txt_aadhar_back_photo
        private val txt_licence_front : AppCompatTextView = itemView.txt_licence_front_photo
        private val txt_licence_back : AppCompatTextView = itemView.txt_aadhar_front_photo


        val vehicleRToEdit : AppCompatEditText = itemView.vehicle_RTO_registration_number_edt
        val vehicleRCEdit : AppCompatEditText = itemView.vehicle_rc_number_edt
        val vehicleColor : AutoCompleteTextView = itemView.vehicle_colour_edt
        val vehicleMakeYear : AutoCompleteTextView = itemView.vehicle_make_year_edt
        val vehicleType : AutoCompleteTextView = itemView.vehicle_type_edt
        private val vehicleBrand : AppCompatEditText = itemView.vehicle_brand_edt
        private val vehicleModel : AppCompatEditText = itemView.vehicle_model_edt

        private val vehicleFront : AppCompatImageView = itemView.vehicle_front_photo
        private val vehicleRC : AppCompatImageView = itemView.vehicle_rc_photo
        private val vehicleInsurance : AppCompatImageView = itemView.vehicle_insurance_photo
        private val vehiclePermit : AppCompatImageView = itemView.vehicle_permit_photo
        private val txt_vehicle_Front : AppCompatTextView = itemView.txt_vehicle_front_photo
        private val txt_vehicle_rc : AppCompatTextView = itemView.txt_vehicle_rc
        private val txt_vehicle_insurance : AppCompatTextView = itemView.txt_vehicle_insurance
        private val txt_vehicle_permit : AppCompatTextView = itemView.txt_vehicle_permit

        val btnUpdateBasic : AppCompatButton = itemView.btn_update_basic
        private val btnUpdateAddress : AppCompatButton = itemView.btn_address_basic
        private val btnVehicleInfo : AppCompatButton = itemView.btn_vehicle_basic

        private val vehicleTypeList: Array<String> = itemView.resources.getStringArray(R.array.vehicleType)
        private val stateItems: Array<String> = itemView.resources.getStringArray(R.array.states)
        private val colorItem : Array<String> = itemView.resources.getStringArray(R.array.vehicleColors)
        private val districtItem : Array<String> = itemView.resources.getStringArray(R.array.districtList)

        val arrayList: ArrayList<String> = ArrayList()
        private var vehicleTypeArray: ArrayAdapter<*> = ArrayAdapter(itemView.context, R.layout.dropdown_items, vehicleTypeList)
        private var colorArray: ArrayAdapter<*> = ArrayAdapter(itemView.context, R.layout.dropdown_items, colorItem)
        private var statesArray: ArrayAdapter<*> = ArrayAdapter(itemView.context, R.layout.dropdown_items, stateItems)
        private var districtArray: ArrayAdapter<*> = ArrayAdapter(itemView.context, R.layout.dropdown_items, districtItem)

        private val years : ArrayList<String> = ArrayList<String>()
        private var makeYearArray: ArrayAdapter<*> = ArrayAdapter(itemView.context, R.layout.dropdown_items, years)

        private val thisYear : Int = Calendar.getInstance().get(Calendar.YEAR)


        init {

            vehicleColor.setAdapter(colorArray)
            vehicleType.setAdapter(vehicleTypeArray)
            vehicleMakeYear.setAdapter(makeYearArray)
            state.setAdapter(statesArray)
            districtNameEdit.setAdapter(districtArray)

            for (i in 1990..thisYear){
                years.add(i.toString())
            }




            fullNameEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    basicInfo[adapterPosition].full_name = fullNameEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            emailEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    val email = emailEdit.text.toString()
                    if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        basicInfo[adapterPosition].email = emailEdit.text.toString().trim()
                        btnUpdateBasic.visibility = View.VISIBLE
                    }
                    else {
                        btnUpdateBasic.visibility = View.GONE
                        emailEdit.requestFocus()
                        emailEdit.error = "Please enter valid email"
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            contactNumberEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (contactNumberEdit.text.toString().length == 10) {
                        basicInfo[adapterPosition].contact_number =
                            contactNumberEdit.text.toString().trim()
                        btnUpdateBasic.visibility = View.VISIBLE
                    }
                    else{
                        btnUpdateBasic.visibility = View.GONE
                        contactNumberEdit.requestFocus()
                        contactNumberEdit.error = "Please enter 10-digit number"
                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            dateOfBirthEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    dateOfBirthEdit.isFocusable = false
                    basicInfo[adapterPosition].date_of_birth = dateOfBirthEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

            houseNumberEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addressInfo[adapterPosition].house_number = houseNumberEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            buildingNameEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addressInfo[adapterPosition].building_name = buildingNameEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            streetNameEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addressInfo[adapterPosition].street_name = streetNameEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            landMarkEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addressInfo[adapterPosition].landmark = landMarkEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            state.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addressInfo[adapterPosition].state = state.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            districtNameEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addressInfo[adapterPosition].district = districtNameEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            pinCodeEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    addressInfo[adapterPosition].pin_code = pinCodeEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

            vehicleRToEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    vehicleInfo[adapterPosition].vehicle_RTO_registration_number = vehicleRToEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            vehicleRCEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    vehicleInfo[adapterPosition].vehicle_rc_number = vehicleRCEdit.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            vehicleColor.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    vehicleInfo[adapterPosition].vehicle_colour = vehicleColor.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            vehicleMakeYear.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    vehicleInfo[adapterPosition].vehicle_make_year = vehicleMakeYear.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            vehicleType.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    vehicleInfo[adapterPosition].vehicle_type = vehicleType.text.toString().trim()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })

            btnUpdateBasic.setOnClickListener {
                if (mListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        mListener!!.onBasicUpdateClick(position)
                    }
                }
                else if (mListener == null) {
                //Log.d("listenerCheck", "Listener null: ")
                }
            }
            btnUpdateAddress.setOnClickListener {
                if (addressListener!=null){
                    val position = adapterPosition
                    if (position!= RecyclerView.NO_POSITION){
                        addressListener!!.onAddressClick(position)
                    }
                }
            }
            btnVehicleInfo.setOnClickListener {
                if (vehicleInfoListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        vehicleInfoListener!!.onVehicleClick(position)
                    }
                }
            }

            dateOfBirthEdit.setOnTouchListener(View.OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= dateOfBirthEdit.right - dateOfBirthEdit.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        showCalender(setDateListener)
                        return@OnTouchListener true
                    }
                }
                false
            })
            dateOfBirthEdit.setOnClickListener { showCalender(setDateListener) }

            setDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val cal : Calendar = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

               // val myFormat = "dd.MM.yyyy" // mention the format you need
                var myFormat = "dd/MM/YYYY"
                val sdf = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    SimpleDateFormat(myFormat, Locale.US)
                } else {

                    myFormat = "dd.MM.yyyy"
                    SimpleDateFormat(myFormat, Locale.US)
                }

                dateOfBirthEdit.setText(String.format( sdf.format(cal.time)))

            }

            adharFront.setOnClickListener {
                if (adharFrontListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        adharFrontListener!!.aadharClick(position)
                    }
                }
            }
            adharBack.setOnClickListener {
                if (adharBackListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        adharBackListener!!.onAdharBackListener(position)
                    }
                }
            }
            licenceFront.setOnClickListener {
                if (licenceFrontClick!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        licenceFrontClick!!.onLicenceFrontClick(position)
                    }
                }
            }
            licenceBack.setOnClickListener {
                if (liceneceBackListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        liceneceBackListener!!.onLicenceBackClick(position)
                    }
                }
            }

            vehicleFront.setOnClickListener {
                if (vehicleFrontListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        vehicleFrontListener!!.onVehicleFrontClick(position)
                    }
                }
            }
            vehicleRC.setOnClickListener {
                if (vehicleRCListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        vehicleRCListener!!.onVehicleRcClick(position)
                    }
                }
            }
            vehicleInsurance.setOnClickListener {
                if (insuranceListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        insuranceListener!!.onVehicleInsuranceClick(position)
                    }
                }
            }
            vehiclePermit.setOnClickListener {
                if (permitListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        permitListener!!.onPermitClick(position)
                    }
                }
            }

            txt_adhar_front.setOnTouchListener(OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txt_adhar_front.right - txt_adhar_front.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        if (adharFrontListener!=null){
                            val position = adapterPosition
                            if (position!=RecyclerView.NO_POSITION){
                                adharFrontListener!!.aadharClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })
            txt_adhar_back.setOnTouchListener(OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txt_adhar_back.right - txt_adhar_back.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        if (adharBackListener!=null){
                            val position = adapterPosition
                            if (position!=RecyclerView.NO_POSITION){
                                adharBackListener!!.onAdharBackListener(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })
            txt_licence_front.setOnTouchListener(OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txt_licence_front.right - txt_licence_front.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        if (licenceFrontClick!=null){
                            val position = adapterPosition
                            if (position!=RecyclerView.NO_POSITION){
                                licenceFrontClick!!.onLicenceFrontClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })
            txt_licence_back.setOnTouchListener(OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txt_licence_back.right - txt_licence_back.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        if (liceneceBackListener!=null){
                            val position = adapterPosition
                            if (position!=RecyclerView.NO_POSITION){
                                liceneceBackListener!!.onLicenceBackClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })

            txt_vehicle_Front.setOnTouchListener(OnTouchListener { _, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txt_vehicle_Front.right - txt_vehicle_Front.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        if (vehicleFrontListener!=null){
                            val position = adapterPosition
                            if (position!=RecyclerView.NO_POSITION){
                                vehicleFrontListener!!.onVehicleFrontClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                Log.d("txtCall", ": from false condition")
                true
            })
            txt_vehicle_rc.setOnTouchListener(OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txt_vehicle_rc.right - txt_vehicle_rc.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        if (vehicleRCListener!=null){
                            val position = adapterPosition
                            if (position!=RecyclerView.NO_POSITION){
                                vehicleRCListener!!.onVehicleRcClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })
            txt_vehicle_insurance.setOnTouchListener(OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txt_vehicle_insurance.right - txt_vehicle_insurance.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        if (insuranceListener!=null){
                            val position = adapterPosition
                            if (position!=RecyclerView.NO_POSITION){
                                insuranceListener!!.onVehicleInsuranceClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })
            txt_vehicle_permit.setOnTouchListener(OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txt_vehicle_permit.right - txt_vehicle_permit.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        if (permitListener!=null){
                            val position = adapterPosition
                            if (position!=RecyclerView.NO_POSITION){
                                permitListener!!.onPermitClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })

            setHint()
            setKeyStyle()
        }

        fun bindData(position: Int){
            /*--- binding basic information -----*/
            if (isBasicInfoShow) {

                fullNameEdit.setText(basicInfo[position].full_name)
                emailEdit.setText(basicInfo[position].email)
                contactNumberEdit.setText(basicInfo[position].contact_number)
                dateOfBirthEdit.setText(basicInfo[position].date_of_birth)

                basicInfoParent.visibility = View.VISIBLE

              //  Log.d("adapterS", "bindData: from if ")
            }

            /*---- binding address information ------*/
            if (isAddressInfoShow) {
                houseNumberEdit.setText(addressInfo[position].house_number)
                buildingNameEdit.setText(addressInfo[position].building_name)
                streetNameEdit.setText(addressInfo[position].street_name)
                landMarkEdit.setText(addressInfo[position].landmark)
                state.setText(addressInfo[position].state)
                districtNameEdit.setText(addressInfo[position].district)
                pinCodeEdit.setText(addressInfo[position].pin_code)

                basicInfoParent.visibility = View.GONE
                addressInfoParent.visibility = View.VISIBLE
            }

            /* ------- binding KYC Details  ---------- */
            if (isKYCInfoShow) {
                val adharFrontURl = kycInfo[position].aadhar_front_photo
                val adharBackURl = kycInfo[position].aadhar_back_photo
                val LicenceFrontURl = kycInfo[position].licence_front_photo
                val LicenceBackURl = kycInfo[position].licence_back_photo

                Glide.with(context).load(adharFrontURl).into(adharFront)
                Glide.with(context).load(adharBackURl).into(adharBack)
                Glide.with(context).load(LicenceFrontURl).into(licenceFront)
                Glide.with(context).load(LicenceBackURl).into(licenceBack)

                basicInfoParent.visibility = View.GONE
                addressInfoParent.visibility = View.GONE
                kycInfoParent.visibility = View.VISIBLE
            }

            /*------ binding vehicle details ------------*/
            if (isVehicleInfoShow) {
                vehicleRToEdit.setText(vehicleInfo[position].vehicle_RTO_registration_number)
                vehicleRCEdit.setText(vehicleInfo[position].vehicle_rc_number)
                vehicleColor.setText(vehicleInfo[position].vehicle_colour)
                vehicleMakeYear.setText(vehicleInfo[position].vehicle_make_year)
                vehicleType.setText(vehicleInfo[position].vehicle_type)
                vehicleBrand.setText(vehicleInfo[position].vehicle_brand)
                vehicleModel.setText(vehicleInfo[position].vehicle_model)

                basicInfoParent.visibility = View.GONE
                addressInfoParent.visibility = View.GONE
                kycInfoParent.visibility = View.GONE
                vehicleInfoParent.visibility = View.VISIBLE
            }

            /*-------------- binding vehicle documents ------ */
            if (isVehicleProfileInfoShow) {
                val vehicleFrontUrl = vehicleProfilePicInfo[position].vehicle_front_photo
                val vehicleBackUrl = vehicleProfilePicInfo[position].vehicle_rc
                val vehicleLeftUrl = vehicleProfilePicInfo[position].vehicle_insurance
                val vehicleRightUrl = vehicleProfilePicInfo[position].vehicle_permit

                Glide.with(context).load(vehicleFrontUrl).into(vehicleFront)
                Glide.with(context).load(vehicleBackUrl).into(vehicleRC)
                Glide.with(context).load(vehicleLeftUrl).into(vehicleInsurance)
                Glide.with(context).load(vehicleRightUrl).into(vehiclePermit)

                basicInfoParent.visibility = View.GONE
                addressInfoParent.visibility = View.GONE
                kycInfoParent.visibility = View.GONE
                vehicleInfoParent.visibility = View.GONE
                vehicleDocumentInfoParent.visibility = View.VISIBLE
            }

        }
        private fun setHint(){
            fullNameEdit.hint = "FirstName     MiddleName     LastName "
            emailEdit.hint = "Enter your email address"
            contactNumberEdit.hint = "Enter your contact number"

            houseNumberEdit.hint = "Enter house number"
            buildingNameEdit.hint = "Enter building name"
            streetNameEdit.hint = "Enter street name"
            landMarkEdit.hint = "Enter landmark"
            state.hint = "Enter state name"
            districtNameEdit.hint = "Enter district name"
            pinCodeEdit.hint = "Enter pin code"

            vehicleRToEdit.hint = "Enter your RTO registration number"
            vehicleRCEdit.hint = "Enter your RC Number"
            vehicleBrand.hint = "Enter your vehicle brand name"
            vehicleModel.hint = "Enter your vehicle model name"

        }

        private fun setKeyStyle(){
            contactNumberEdit.inputType =InputType.TYPE_CLASS_NUMBER
            val maxLength = 10
            contactNumberEdit.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
    }

    private fun showCalender(dateListener: DatePickerDialog.OnDateSetListener) {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog: DatePickerDialog = DatePickerDialog(
            context, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            setDateListener, year, month, day
        )

        datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        datePickerDialog.show()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun showBasicInfo(list : ArrayList<BasicRequestedInfo>){
        this.isBasicInfoShow = true
        this.basicInfo.addAll(list)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun showAddressInfo(list: ArrayList<AddressRequestedInfo>){
        this.isAddressInfoShow = true
        this.addressInfo.addAll(list)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun showKYCInfo(list: ArrayList<KycRequestedInfo>){
        this.isKYCInfoShow = true
        this.kycInfo.addAll(list)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun showVehicleInfo(list: ArrayList<VehicleRequestedInfo>){
        this.isVehicleInfoShow = true
        this.vehicleInfo.addAll(list)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun showVehicleProfilePic(list: ArrayList<VehicleProfilePic>){
        this.isVehicleProfileInfoShow = true
        this.vehicleProfilePicInfo.addAll(list)
        notifyDataSetChanged()

    }

    interface OnBasicUpdateClick{
        fun onBasicUpdateClick(position: Int)
    }

    fun setOnBasicClickListener(listener: DriverProfileActivity) {
        mListener = listener
    }

}

