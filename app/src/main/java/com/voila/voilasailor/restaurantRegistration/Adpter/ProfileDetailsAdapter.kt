package com.voila.voilasailor.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.OwnerInfo
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.RestaurantInfo
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.RestaurantProfilePic
import kotlinx.android.synthetic.main.owne_details_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProfileDetailsAdapter(var context: Context) : RecyclerView.Adapter<ProfileDetailsAdapter.ViewHolder>() {

    var ownerDetailsList : ArrayList<OwnerInfo> = ArrayList()
    var restaurantDetailsList : ArrayList<RestaurantInfo> = ArrayList()
    var restaurantDocsList : ArrayList<RestaurantProfilePic> = ArrayList()
    private lateinit var setDateListener : DatePickerDialog.OnDateSetListener


    private var isOwnerDetailsShow  : Boolean = false
    var isRestaurantDetailsShow : Boolean = false
    var isRestaurantDocsShow : Boolean = false

    private var mListener: OnItemClickListener? = null
    private var outdoorListener  : OnOutdoorClickListener? = null
    private var licenceListener : OnLicenceClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(layoutInflater.inflate(R.layout.owne_details_layout,parent,false))

    }

    override fun onBindViewHolder(holder: ProfileDetailsAdapter.ViewHolder, position: Int) {

        when {
            isOwnerDetailsShow -> {
                holder.ownerDetailParentLayout.visibility = View.VISIBLE
                holder.restaurantProfileParentLayout.visibility = View.GONE
                holder.restaurantDocsParentLayout.visibility = View.GONE

                holder.ownerNameEdit.setText(ownerDetailsList[position].owner_name)
                holder.ownerEmailEdit.setText(ownerDetailsList[position].owner_email)
                holder.ownerContactNoEdit.setText(ownerDetailsList[position].owner_contact_no)
                holder.ownerCurrentAddressEdit.setText(ownerDetailsList[position].owner_current_address)
                holder.ownerPermanentAddressEdit.setText(ownerDetailsList[position].owner_permanent_address)
            }
            isRestaurantDetailsShow -> {
                holder.ownerDetailParentLayout.visibility = View.GONE
                holder.restaurantProfileParentLayout.visibility = View.VISIBLE
                holder.restaurantDocsParentLayout.visibility = View.GONE

                holder.restaurantNameEdit.setText(restaurantDetailsList[position].restaurant_name)
                holder.restaurantEmailEdit.setText(restaurantDetailsList[position].restaurant_email)
                holder.restaurantContactNoEdit.setText(restaurantDetailsList[position].restaurant_contact_no)
                holder.restaurantOpeningTimeEdit.setText(restaurantDetailsList[position].restaurant_opening_time)
                holder.restaurantClosingTimeEdit.setText(restaurantDetailsList[position].restaurant_close_time)
                holder.restaurantWebsiteEdit.setText(restaurantDetailsList[position].restaurant_website)
                holder.restaurantEstablishmentYearEdit.setText(restaurantDetailsList[position].restaurant_establishment_year)
                holder.restaurantCuisinesTypeEdit.setText(restaurantDetailsList[position].restaurant_cuisines_type)

            }
            isRestaurantDocsShow -> {
                holder.ownerDetailParentLayout.visibility = View.GONE
                holder.restaurantProfileParentLayout.visibility = View.GONE
                holder.restaurantDocsParentLayout.visibility = View.VISIBLE

                val indoorPhoto : String = restaurantDocsList[position].restaurant_indoor_photo
                val outdoorPhoto : String = restaurantDocsList[position].restaurant_outdoor_photo
                val licencePhoto : String = restaurantDocsList[position].restaurant_licence_photo

                Glide.with(context).load(indoorPhoto).into(holder.restaurantIndoorImage)
                Glide.with(context).load(outdoorPhoto).into(holder.restaurantOutdoorImage)
                Glide.with(context).load(licencePhoto).into(holder.restaurantLicenceImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return when {
            isOwnerDetailsShow -> {
                ownerDetailsList.size
            }
            isRestaurantDetailsShow -> {
                restaurantDetailsList.size
            }
            isRestaurantDocsShow -> {
                restaurantDocsList.size
            }
            else -> 0
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        /*--- parent layout to view visible and invisible --*/
        val ownerDetailParentLayout :LinearLayoutCompat = itemView.owner_details_parent_layout
        val restaurantProfileParentLayout: LinearLayoutCompat = itemView.restaurant_details_parent_layout
        val restaurantDocsParentLayout : LinearLayoutCompat = itemView.restaurant_docs_parent_payout

        /*---- owner edit ----*/
        val ownerNameEdit : AppCompatEditText = itemView.owner_name_edt_info
        val ownerEmailEdit : AppCompatEditText = itemView.owner_email_edt_info
        val ownerContactNoEdit : AppCompatEditText = itemView.owner_contact_edt_info
        val ownerCurrentAddressEdit : AppCompatEditText = itemView.owner_current_address_edt_info
        val ownerPermanentAddressEdit : AppCompatEditText = itemView.owner_permant_address_edt_info

        /*--- Restaurant edit ------ */
        val restaurantNameEdit : AppCompatEditText = itemView.restaurant_name_edt_info
        val restaurantEmailEdit : AppCompatEditText = itemView.restaurant_email_edt_info
        val restaurantContactNoEdit : AppCompatEditText = itemView.restaurant_contact_no_edt_info
        val restaurantOpeningTimeEdit : AppCompatEditText = itemView.restaurant_opening_time_edt_info
        val restaurantClosingTimeEdit : AppCompatEditText = itemView.restaurant_close_time_edt_info
        val restaurantWebsiteEdit : AppCompatEditText = itemView.restaurant_website_edt_info
        val restaurantEstablishmentYearEdit : AppCompatEditText = itemView.restaurant_establishment_year_edt_info
        val restaurantCuisinesTypeEdit : AppCompatEditText = itemView.restaurant_cuisines_type_edt_info

        /*------ Restaurant docs ----- */
        val restaurantIndoorImage : AppCompatImageView = itemView.restaurant_indoor_photo
        val restaurantOutdoorImage : AppCompatImageView = itemView.restaurant_outdoor_photo
        val restaurantLicenceImage : AppCompatImageView = itemView.restaurant_licence_photo

        /*------  Restaurant docs name ---- */
        private val txtRestaurantIndoor : AppCompatTextView = itemView.txt_restaurant_indoor
        private val txtRestaurantOutdoor : AppCompatTextView = itemView.txt_restaurant_outdoor
        private val txtRestaurantLicence : AppCompatTextView = itemView.txt_restaurant_licence

        init {

            //owner details
            if (isOwnerDetailsShow){
                ownerNameEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        ownerDetailsList[adapterPosition].ownerName = ownerNameEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
                ownerEmailEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val email = ownerEmailEdit.text.toString()
                        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            ownerDetailsList[adapterPosition].ownerEmail = ownerEmailEdit.text.toString()
                        }
                        else {
                            ownerEmailEdit.requestFocus()
                            ownerEmailEdit.error = "Please enter valid email"
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
                ownerContactNoEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (ownerContactNoEdit.text.toString().length == 10){
                            ownerDetailsList[adapterPosition].ownerContactNo = ownerContactNoEdit.text.toString()
                        }
                        else{
                            ownerContactNoEdit.requestFocus()
                            ownerContactNoEdit.error = "Please enter 10-digit number"
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
                ownerCurrentAddressEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        ownerDetailsList[adapterPosition].ownerCurrentAddress = ownerCurrentAddressEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
                ownerPermanentAddressEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        ownerDetailsList[adapterPosition].ownerPermanentAddress = ownerPermanentAddressEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            }


            /*--- Restaurant Details ----*/

            restaurantNameEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantName = restaurantNameEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
            restaurantEmailEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        val email = restaurantEmailEdit.text.toString()
                        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            restaurantDetailsList[adapterPosition].restaurantEmail = restaurantEmailEdit.text.toString()
                        }
                        else {
                            restaurantEmailEdit.requestFocus()
                            restaurantEmailEdit.error = "Please enter valid email"
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantContactNoEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (restaurantContactNoEdit.text.toString().length == 10){
                            restaurantDetailsList[adapterPosition].restaurantContactNo = restaurantContactNoEdit.text.toString()
                        }
                        else{
                            restaurantContactNoEdit.error = "Please Enter 10-digit number"
                            restaurantContactNoEdit.requestFocus()
                        }
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantOpeningTimeEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantOpeningTime = restaurantOpeningTimeEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantClosingTimeEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantClosingTime = restaurantClosingTimeEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantWebsiteEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantWebsite = restaurantWebsiteEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantEstablishmentYearEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantEstablishmentYear = restaurantEstablishmentYearEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantCuisinesTypeEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantCuisinesType = restaurantCuisinesTypeEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })

            restaurantIndoorImage.setOnClickListener {
                if (mListener!=null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        mListener!!.onItemClick(position)
                    }
                }
            }
            restaurantOutdoorImage.setOnClickListener {
                if (outdoorListener!=null){
                    val position = adapterPosition
                    if (position!= RecyclerView.NO_POSITION){
                        outdoorListener!!.onOutDoorItemClick(position)
                    }
                }
            }
            restaurantLicenceImage.setOnClickListener {
                if (licenceListener!=null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        licenceListener!!.onLicenceItemClick(position)
                    }
                }
            }

            txtRestaurantIndoor.setOnTouchListener(View.OnTouchListener { _, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txtRestaurantIndoor.right - txtRestaurantIndoor.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        if (mListener!=null){
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION){
                                mListener!!.onItemClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })
            txtRestaurantOutdoor.setOnTouchListener(View.OnTouchListener { _, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txtRestaurantOutdoor.right - txtRestaurantOutdoor.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        if (outdoorListener!=null){
                            val position = adapterPosition
                            if (position!= RecyclerView.NO_POSITION){
                                outdoorListener!!.onOutDoorItemClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })
            txtRestaurantLicence.setOnTouchListener(View.OnTouchListener { _, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= txtRestaurantLicence.right - txtRestaurantLicence.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        if (licenceListener!=null){
                            val position = adapterPosition
                            if (position != RecyclerView.NO_POSITION){
                                licenceListener!!.onLicenceItemClick(position)
                            }
                        }
                        return@OnTouchListener true
                    }
                }
                true
            })

            setHint()
            setInputTypeOrLen()
        }

        private fun setHint() {
            ownerNameEdit.hint = "FirstName   MiddleName   LastName"
            ownerEmailEdit.hint = "Enter your email address"
            ownerContactNoEdit.hint =  "Enter your 10-digit number"
            ownerCurrentAddressEdit.hint = "Enter your current address"
            ownerPermanentAddressEdit.hint = "Enter your permanent address"

            restaurantNameEdit.hint = "Enter restaurant name"
            restaurantEmailEdit.hint = "Enter restaurant email"
            restaurantContactNoEdit.hint = "Enter restaurant contact number"
            restaurantOpeningTimeEdit.hint = "Enter restaurant opening time"
            restaurantClosingTimeEdit.hint = "Enter restaurant closing time"
            restaurantWebsiteEdit.hint = "Enter restaurant website "
            restaurantEstablishmentYearEdit.hint = "Enter restaurant establishment year"
            restaurantCuisinesTypeEdit.hint = "Enter restaurant cuisines type"
        }

      @RequiresApi(Build.VERSION_CODES.N)
      private fun setInputTypeOrLen(){
            ownerContactNoEdit.inputType = InputType.TYPE_CLASS_NUMBER
            val maxLength = 10
            ownerContactNoEdit.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))

          restaurantContactNoEdit.inputType = InputType.TYPE_CLASS_NUMBER
          restaurantContactNoEdit.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))

          setTimeInterface()
          setClosingTimeInterface()
          setCalanderInterface()
      }
        @SuppressLint("ClickableViewAccessibility")
        fun setTimeInterface(){
            restaurantOpeningTimeEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_watch, 0);
            restaurantOpeningTimeEdit.isFocusable = false
            restaurantOpeningTimeEdit.setOnTouchListener(
                View.OnTouchListener { v, event ->
                    val DRAWABLE_LEFT = 0
                    val DRAWABLE_TOP = 1
                    val DRAWABLE_RIGHT = 2
                    val DRAWABLE_BOTTOM = 3
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.rawX >= restaurantOpeningTimeEdit.right - restaurantOpeningTimeEdit.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                            getDate(restaurantOpeningTimeEdit)
                            return@OnTouchListener true
                        }
                    }
                    false
                },
            )
        }

        private fun setClosingTimeInterface() {
            restaurantClosingTimeEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_watch, 0);
            restaurantClosingTimeEdit.isFocusable = false
            restaurantClosingTimeEdit.setOnTouchListener(
                View.OnTouchListener { v, event ->
                    val DRAWABLE_LEFT = 0
                    val DRAWABLE_TOP = 1
                    val DRAWABLE_RIGHT = 2
                    val DRAWABLE_BOTTOM = 3
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.rawX >= restaurantClosingTimeEdit.right - restaurantClosingTimeEdit.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                            getDate(restaurantClosingTimeEdit)
                            return@OnTouchListener true
                        }
                    }
                    false
                },
            )
        }
        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("ClickableViewAccessibility")
        fun setCalanderInterface(){
            restaurantEstablishmentYearEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);
            restaurantEstablishmentYearEdit.isFocusable = false
            restaurantEstablishmentYearEdit.setOnTouchListener(View.OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= restaurantEstablishmentYearEdit.right - restaurantEstablishmentYearEdit.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        showCalender(setDateListener)
                        return@OnTouchListener true
                    }
                }
                false
            })
            restaurantEstablishmentYearEdit.setOnClickListener { showCalender(setDateListener) }

            setDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val cal : Calendar = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

//                val myFormat = "dd.MM.yyyy" // mention the format you need
                var myFormat = "dd/MM/YYYY"
                val sdf = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    SimpleDateFormat(myFormat, Locale.US)
                } else {

                    myFormat = "dd.MM.yyyy"
                    SimpleDateFormat(myFormat, Locale.US)
                }

                restaurantEstablishmentYearEdit.setText(String.format( sdf.format(cal.time)))

            }
        }

        private var tHour = 0
        var tMinute:Int = 0
        private fun getDate(titleEdit: AppCompatEditText) {
            val timePickerDialog = TimePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                { timePicker, i, i1 ->
                    tHour = i
                    tMinute = i1
                    val calendar = Calendar.getInstance()
                    calendar[0, 0, 0, tHour] = tMinute
                    // Log.d("timerSelect", "onTimeSet: " + DateFormat.format("hh:mm aa", calendar))
                    titleEdit.setText(DateFormat.format("hh:mm aa", calendar))
                }, 12, 0, false
            )
            timePickerDialog.updateTime(tHour, tMinute)
            timePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePickerDialog.show()

        }
        private fun showCalender(dateListener: DatePickerDialog.OnDateSetListener) {
            val calendar : Calendar = Calendar.getInstance()
            val year : Int = calendar.get(Calendar.YEAR)
            val month : Int = calendar.get(Calendar.MONTH)
            val day : Int = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog : DatePickerDialog = DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                setDateListener,year,month,day)

            datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            datePickerDialog.show()
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    fun showOwnerDetails(list:ArrayList<OwnerInfo>){
        this.ownerDetailsList.addAll(list)
        this.isOwnerDetailsShow = true
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showRestaurantDetails(list:ArrayList<RestaurantInfo>){
        this.restaurantDetailsList.addAll(list)
        this.isRestaurantDetailsShow = true
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showRestaurantDocsDetails(list:ArrayList<RestaurantProfilePic>){
        this.restaurantDocsList.addAll(list)
        this.isRestaurantDocsShow = true
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun restaurantIndoorClickListener(listener : OnItemClickListener){

        mListener = listener
    }

    interface OnOutdoorClickListener{
        fun onOutDoorItemClick(position: Int)
    }

    fun restaurantOutdoorClickListener(listener : OnOutdoorClickListener){
        outdoorListener = listener
    }

    interface OnLicenceClickListener{
        fun onLicenceItemClick(position: Int)
    }

    fun restaurantLicenceClickListener(listener: OnLicenceClickListener){
        licenceListener = listener
    }
}