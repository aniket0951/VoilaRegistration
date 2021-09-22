package com.voila.voilasailor.driverRegistration.Adapter


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.voila.voilasailor.Model.RestaurantOwnerRequiredDoc
import com.voila.voilasailor.R
import com.voila.voilasailor.driverRegistration.DriverRegistrationActivity
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.NeedToProcessComplete
import kotlinx.android.synthetic.main.restaurant_details_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Toast

import android.util.Patterns

import android.widget.EditText
import android.text.InputFilter
import android.text.InputFilter.LengthFilter


class DriverRegistrationAdapter(var context: Context) : RecyclerView.Adapter<DriverRegistrationAdapter.ViewHolder>() {

    var ownerRequiredDoc: ArrayList<RestaurantOwnerRequiredDoc> = ArrayList<RestaurantOwnerRequiredDoc>()
    var needToProcessComplete : ArrayList<NeedToProcessComplete> = ArrayList<NeedToProcessComplete>()

    private var mListener: OnItemClickListener? = null
    var isAutoComplete = ObservableField<String>()

    var isImageLayoutShow : Boolean = false


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DriverRegistrationAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.driver_registration_layout, parent, false))

    }

    override fun onBindViewHolder(holder: DriverRegistrationAdapter.ViewHolder, position: Int) {
        holder.bindTheData(position)
    }

    override fun getItemCount(): Int {
        return needToProcessComplete.size
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val autoCompleteTextView : AutoCompleteTextView = itemView.auto_complete_text
        private val vehicleTypeList: Array<String> = itemView.resources.getStringArray(R.array.vehicleType)
        private val stateItems: Array<String> = itemView.resources.getStringArray(R.array.states)
        private val colorItem : Array<String> = itemView.resources.getStringArray(R.array.vehicleColors)
        private val districtItem : Array<String> = itemView.resources.getStringArray(R.array.districtList)

        private lateinit var setDateListener : DatePickerDialog.OnDateSetListener

        val title : AppCompatTextView = itemView.title
        var titleEdit : AppCompatEditText = itemView.edt_info

        private val editTextLayout : TextInputLayout = itemView.edt_text_layout
        private val listItemLayout : TextInputLayout = itemView.list_item_layout

        private val docsTitle : AppCompatTextView = itemView.doc_title
        private val docsImage : AppCompatImageView = itemView.docs_image

        private var docsLayout : LinearLayoutCompat = itemView.doc_main_layout
        var editLayout : LinearLayoutCompat = itemView.edit_main_layout

        val arrayList: ArrayList<String> = ArrayList()
        private var arrayAdapter: ArrayAdapter<*> = ArrayAdapter(itemView.context, R.layout.dropdown_items, arrayList)

        private val years : ArrayList<String> = ArrayList<String>()

        private val thisYear : Int = Calendar.getInstance().get(Calendar.YEAR)


        init {

            for (i in 1990..thisYear){
                years.add(i.toString())
            }

            titleEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    needToProcessComplete[adapterPosition].editText = titleEdit.text.toString()
                   //  Log.d("adapterText", "onTextChanged: " +  needToProcessComplete[adapterPosition] + titleEdit.text.toString() )
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

            docsImage.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(position)
                    }

                }

            }

            autoCompleteTextView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    needToProcessComplete[adapterPosition].autoCompleteTextView = autoCompleteTextView.text.toString()
                     //Log.d("editValue", "onTextChanged: " + needToProcessComplete[adapterPosition] + autoCompleteTextView.text.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

        }


        fun bindTheData(position: Int){

            autoCompleteTextView.hint = needToProcessComplete[position].required_docs_name
            autoCompleteTextView.setText(needToProcessComplete[position].autoCompleteTextView)

            if (isImageLayoutShow){
                editLayout.visibility = View.GONE
                docsLayout.visibility = View.VISIBLE
                docsTitle.text = needToProcessComplete[position].required_docs_name
            }
            else{

                editLayout.visibility = View.VISIBLE
                docsLayout.visibility = View.GONE


                if (needToProcessComplete[position].required_docs_type == "list" ){
                    editTextLayout.visibility = View.GONE
                    listItemLayout.visibility = View.VISIBLE

                }

                if (needToProcessComplete[position].required_docs_type == "calendar"){
                    setDateInterface()
                }

                title.text = needToProcessComplete[position].required_docs_name
                titleEdit.hint = needToProcessComplete[position].required_docs_name
                titleEdit.setText(needToProcessComplete[position].editText)

                titleEdit.setOnTouchListener(View.OnTouchListener { view, motionEvent -> // your code here....
                    if (needToProcessComplete[adapterPosition].required_docs_input.equals("text", true)) {
                        titleEdit.inputType = InputType.TYPE_CLASS_TEXT
                    }
                    if (needToProcessComplete[adapterPosition].required_docs_input.equals("number", true)) {
                        titleEdit.inputType = InputType.TYPE_CLASS_NUMBER
                        val maxLength = 10
                        titleEdit.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
                    }
                    if (needToProcessComplete[adapterPosition].required_docs_input.equals("email", true)) {
                        titleEdit.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                    false
                })

                autoCompleteTextView.setOnClickListener {
                    when {
                        needToProcessComplete[position].required_docs_name.equals("State",true) -> {
                            arrayList.clear()
                            arrayList.addAll(stateItems)
                            autoCompleteTextView.setAdapter(arrayAdapter)
                            autoCompleteTextView.showDropDown()
                        }
                        needToProcessComplete[position].required_docs_name.equals("District",true) -> {
                            arrayList.clear()
                            arrayList.addAll(districtItem)
                            autoCompleteTextView.setAdapter(arrayAdapter)
                            autoCompleteTextView.showDropDown()
                        }
                        needToProcessComplete[position].required_docs_name.equals("Vehicle Type",false) -> {
                            arrayList.clear()
                            arrayList.addAll(vehicleTypeList)
                            autoCompleteTextView.setAdapter(arrayAdapter)
                            autoCompleteTextView.showDropDown()
                        }
                        needToProcessComplete[position].required_docs_name.equals("Vehicle Colour",false) ->{
                            arrayList.clear()
                            arrayList.addAll(colorItem)
                            autoCompleteTextView.setAdapter(arrayAdapter)
                            autoCompleteTextView.showDropDown()
                        }
                        needToProcessComplete[position].required_docs_name.equals("Vehicle Make Year",true) -> {
                            arrayList.clear()
                            arrayList.addAll(years)
                            autoCompleteTextView.setAdapter(arrayAdapter)
                            autoCompleteTextView.showDropDown()
                        }
                    }
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun setDateInterface(){

            titleEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);
            titleEdit.isFocusable = false
            titleEdit.setOnTouchListener(View.OnTouchListener { v, event ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= titleEdit.right - titleEdit.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                        showCalender(setDateListener)
                        return@OnTouchListener true
                    }
                }
                false
            })
            titleEdit.setOnClickListener { showCalender(setDateListener) }

            setDateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val cal : Calendar = Calendar.getInstance()
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    var myFormat = "dd/MM/YYYY" // mention the format you need
                    val sdf = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        SimpleDateFormat(myFormat, Locale.US)
                    } else {

                        myFormat = "dd/MM/yyyy"
                        SimpleDateFormat(myFormat, Locale.US)
                    }

                titleEdit.setText(String.format( sdf.format(cal.time)))

                }
        }
    }


    private fun showCalender(setDateListener: DatePickerDialog.OnDateSetListener) {

        if (isAutoComplete.get().toString().equals("Year",true)){

            val calendar : Calendar = Calendar.getInstance()
            val year : Int = calendar.get(Calendar.YEAR)
            val month : Int = calendar.get(Calendar.MONTH)
            val day : Int = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog : DatePickerDialog = DatePickerDialog(context,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                setDateListener,year,month,day)

            datePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            datePickerDialog.show()
        }
        else {

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
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: DriverRegistrationActivity) {
        mListener = listener
    }


    fun addOwnerDetails(list:ArrayList<RestaurantOwnerRequiredDoc>){
        this.ownerRequiredDoc.addAll(list)
    }

    fun trackRegistrationProcess(list: ArrayList<NeedToProcessComplete>){
        this.needToProcessComplete.addAll(list)
    }

    fun addRestaurantImages(list: ArrayList<NeedToProcessComplete>){
        isImageLayoutShow = true
        this.needToProcessComplete.addAll(list)

    }

}