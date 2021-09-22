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
import com.voila.voilasailor.Model.RestaurantOwnerRequiredDoc
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.NeedToProcessComplete
import kotlinx.android.synthetic.main.restaurant_details_layout.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RestaurantDetailsAdapter(val context: Context) : RecyclerView.Adapter<RestaurantDetailsAdapter.ViewHolder>(){

    var ownerRequiredDoc: ArrayList<RestaurantOwnerRequiredDoc> = ArrayList<RestaurantOwnerRequiredDoc>()
    var needToProcessComplete : ArrayList<NeedToProcessComplete> = ArrayList<NeedToProcessComplete>()

    private var mListener: OnItemClickListener? = null
    private lateinit var setDateListener : DatePickerDialog.OnDateSetListener


    var isImageLayoutShow : Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.restaurant_details_layout, parent, false))

    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RestaurantDetailsAdapter.ViewHolder, position: Int) {

        if (isImageLayoutShow){
            holder.editLayout.visibility = View.GONE
            holder.docsLayout.visibility = View.VISIBLE

            holder.docsTitle.text = needToProcessComplete[position].required_docs_name
        }
        else{

            holder.editLayout.visibility = View.VISIBLE
            holder.docsLayout.visibility = View.GONE

            if (needToProcessComplete[position].required_docs_type.equals("time",true)){
                holder.setTimeInterface()
                holder.titleEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_watch, 0);
                holder.titleEdit.isFocusable = false
            }

            if (needToProcessComplete[position].required_docs_type.equals("calendar",true)){
                holder.setCalanderInterface()
                holder.titleEdit.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_calendar,0)
                holder.titleEdit.isFocusable = false
            }

            holder.title.text = needToProcessComplete[position].required_docs_name
            holder.titleEdit.hint = needToProcessComplete[position].required_docs_name
            holder.titleEdit.setText(needToProcessComplete[position].editText)
        }


    }



    override fun getItemCount(): Int {
        return needToProcessComplete.size
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        // edit layout use title and edit text
        val title : AppCompatTextView = itemView.title
        var titleEdit : AppCompatEditText = itemView.edt_info

        // docs layout use title and docs image
        val docsTitle : AppCompatTextView = itemView.doc_title
        private val docsImage : AppCompatImageView = itemView.docs_image

        //parent layout
        var docsLayout : LinearLayoutCompat = itemView.doc_main_layout
        var editLayout : LinearLayoutCompat = itemView.edit_main_layout


        init {

            titleEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    needToProcessComplete[adapterPosition].editText = titleEdit.text.toString()
                   // Log.d("adapterText", "onTextChanged: " +  needToProcessComplete[adapterPosition] + titleEdit.text.toString() )
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
                   // Log.d("position", ": recycler view not position")
                }
              //  Log.d("position", ": adapter position null")
            }

            titleEdit.setOnTouchListener(View.OnTouchListener { view, motionEvent -> // your code here....
                if (needToProcessComplete[adapterPosition].required_docs_input.equals("text", true)) {
                    titleEdit.inputType = InputType.TYPE_CLASS_TEXT
                }
                if (needToProcessComplete[adapterPosition].required_docs_input.equals("number", true)) {
                   titleEdit.inputType = InputType.TYPE_CLASS_NUMBER
                    val maxLength = 10
                    titleEdit.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
                }
                if (needToProcessComplete[adapterPosition].required_docs_input.equals("email", true)) {
                   titleEdit.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }
//            Log.d("inputChange", "onBindViewHolder: ${needToProcessComplete[position].required_docs_name}")
                false
            })

        }

        @SuppressLint("ClickableViewAccessibility")
        fun setTimeInterface(){
            titleEdit.setOnTouchListener(
                View.OnTouchListener { v, event ->
                    val DRAWABLE_LEFT = 0
                    val DRAWABLE_TOP = 1
                    val DRAWABLE_RIGHT = 2
                    val DRAWABLE_BOTTOM = 3
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.rawX >= titleEdit.right - titleEdit.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                            getDate(titleEdit)
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
            titleEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_calendar, 0);

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

//                val myFormat = "dd.MM.yyyy" // mention the format you need
                var myFormat = "dd/MM/YYYY"
                val sdf = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    SimpleDateFormat(myFormat, Locale.US)
                } else {

                     myFormat = "dd.MM.yyyy"
                    SimpleDateFormat(myFormat, Locale.US)
                }

                titleEdit.setText(String.format( sdf.format(cal.time)))

            }
        }
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

    private var tHour = 0
    var tMinute:Int = 0
    fun getDate(titleEdit: AppCompatEditText) {
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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


}