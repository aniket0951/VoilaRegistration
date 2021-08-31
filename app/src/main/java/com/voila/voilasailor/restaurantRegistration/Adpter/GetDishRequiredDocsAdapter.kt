package com.voila.voilasailor.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.ResultData
import kotlinx.android.synthetic.main.get_dish_required_docs_layout.view.*

class GetDishRequiredDocsAdapter(var context: Context) : RecyclerView.Adapter<GetDishRequiredDocsAdapter.ViewHolder>() {

    var dishRequiredItems : ArrayList<ResultData> = ArrayList()
    private var mListener: OnItemClickListener? = null

    var isImageSelected : Boolean = false

    private var lastSelectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.get_dish_required_docs_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.autoCompleteTextView.hint = dishRequiredItems[position].required_docs_name
        holder.autoCompleteTextView.setText(dishRequiredItems[position].autoCompleteTextView)

        when (dishRequiredItems[position].required_docs_type) {
            "list" -> {
                holder.docTitle.text = dishRequiredItems[position].required_docs_name
                holder.simpleEditLayout.visibility = View.GONE
                holder.listItemLayout.visibility = View.VISIBLE
            }
            "radio" -> {
                holder.radioButton.text = dishRequiredItems[position].required_docs_name
                holder.simpleEditLayout.visibility = View.GONE
                holder.listItemLayout.visibility = View.GONE
                holder.docTitle.visibility = View.GONE
                holder.radioLayout.visibility = View.VISIBLE
            }
            "image" -> {
                holder.imgTitle.text = dishRequiredItems[position].required_docs_name
                holder.docsMainLayout.visibility = View.VISIBLE
                holder.simpleEditLayout.visibility = View.GONE
                holder.listItemLayout.visibility = View.GONE
                holder.docTitle.visibility = View.GONE
                holder.radioLayout.visibility = View.GONE
            }
            else -> {
                holder.docTitle.text = dishRequiredItems[position].required_docs_name
                holder.simpleEditLayout.visibility = View.VISIBLE
                holder.listItemLayout.visibility = View.GONE

                //set hint to edit text
                holder.titleEdit.hint = dishRequiredItems[position].required_docs_name
                holder.titleEdit.setText(dishRequiredItems[position].editText)
            }
        }


        //to radio button
        holder.radioButton.isChecked = lastSelectedPosition == position

        /*-- if image is selected successfully ----*/
        if (isImageSelected) holder.docImage.setImageResource(R.drawable.ic_check)
        else holder.docImage.setImageResource(R.drawable.ic_add)
    }

    override fun getItemCount(): Int {
     return  dishRequiredItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val autoCompleteTextView : AutoCompleteTextView = itemView.auto_complete_text
        private val dishItems: Array<String> = itemView.resources.getStringArray(R.array.menuItems)

        val docTitle : AppCompatTextView = itemView.title
        val simpleEditLayout : TextInputLayout = itemView.simple_edit_layout
        val listItemLayout : TextInputLayout = itemView.list_item_layout
        val radioLayout : LinearLayoutCompat = itemView.radio_layout
        val radioButton : RadioButton = itemView.radioButton1
        val docsMainLayout : LinearLayoutCompat = itemView.doc_main_layout
        val imgTitle : AppCompatTextView = itemView.doc_title
        val titleEdit : AppCompatEditText = itemView.edt_info
        val docImage : AppCompatImageView = itemView.docs_image

        private val arrayAdapter = ArrayAdapter(itemView.context,R.layout.dropdown_items,dishItems)

        init {
            autoCompleteTextView.setAdapter(arrayAdapter)

            /*--- to get edit text value -----*/
            titleEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    dishRequiredItems[adapterPosition].editText = titleEdit.text.toString()
                   // Log.d("editValue", "onTextChanged: " + dishRequiredItems[adapterPosition] + titleEdit.text.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

            /*--- to get dish items value ------*/
            autoCompleteTextView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    dishRequiredItems[adapterPosition].autoCompleteTextView = autoCompleteTextView.text.toString()
                  //  Log.d("editValue", "onTextChanged: " + dishRequiredItems[adapterPosition] + autoCompleteTextView.text.toString())
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })

            /*------ select dish type by radio ------ */
            radioButton.setOnClickListener {
                val copyOfLastCheckedPosition: Int = lastSelectedPosition
                lastSelectedPosition = adapterPosition
                notifyItemChanged(copyOfLastCheckedPosition)
                notifyItemChanged(lastSelectedPosition)

                val t = radioButton.text.toString()
                    .also { dishRequiredItems[layoutPosition].dishTypeSelected = it }

//                Log.d(
//                    "editValue",
//                    "RadioButton: $t"
//                )
            }

            /*--------- To image upload ----------*/
            docImage.setOnClickListener {
                if (mListener != null) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        mListener!!.onItemClick(position)
                    }
                }
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDishRequiredItems(list: ArrayList<ResultData>) {
        this.dishRequiredItems = list
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    fun isImageSelected(position: Int){
        this.isImageSelected = true
        notifyItemChanged(position)
    }
}