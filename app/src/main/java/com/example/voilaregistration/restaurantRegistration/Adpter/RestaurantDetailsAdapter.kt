package com.example.voilaregistration.restaurantRegistration.Adpter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.voilaregistration.Model.RestaurantOwnerRequiredDoc
import com.example.voilaregistration.R
import com.example.voilaregistration.restaurantRegistration.RestaurantModel.NeedToProcessComplete
import kotlinx.android.synthetic.main.restaurant_details_layout.view.*

class RestaurantDetailsAdapter(val context: Context) : RecyclerView.Adapter<RestaurantDetailsAdapter.ViewHolder>(){

    var ownerRequiredDoc: ArrayList<RestaurantOwnerRequiredDoc> = ArrayList<RestaurantOwnerRequiredDoc>()
    var needToProcessComplete : ArrayList<NeedToProcessComplete> = ArrayList<NeedToProcessComplete>()

    private var mListener: OnItemClickListener? = null


    var isImageLayoutShow : Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.restaurant_details_layout, parent, false))

    }

    override fun onBindViewHolder(holder: RestaurantDetailsAdapter.ViewHolder, position: Int) {

        if (isImageLayoutShow){
            holder.editLayout.visibility = View.GONE
            holder.docsLayout.visibility = View.VISIBLE

            holder.docsTitle.text = needToProcessComplete[position].required_docs_name
        }
        else{

            holder.editLayout.visibility = View.VISIBLE
            holder.docsLayout.visibility = View.GONE

            holder.title.text = needToProcessComplete[position].required_docs_name
            holder.titleEdit.hint = needToProcessComplete[position].required_docs_name
            holder.titleEdit.setText(needToProcessComplete[position].editText)
        }


    }

    override fun getItemCount(): Int {
        return needToProcessComplete.size
    }

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


        }

        fun setImage(imageResources: Int){
            this.docsImage.setImageResource(imageResources)
        }
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