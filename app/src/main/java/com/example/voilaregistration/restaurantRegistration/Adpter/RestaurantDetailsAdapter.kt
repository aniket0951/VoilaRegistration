package com.example.voilaregistration.restaurantRegistration.Adpter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voilaregistration.Model.RestaurantOwnerRequiredDoc
import com.example.voilaregistration.R
import kotlinx.android.synthetic.main.restaurant_details_layout.view.*

class RestaurantDetailsAdapter(val context: Context) : RecyclerView.Adapter<RestaurantDetailsAdapter.ViewHolder>(){

    var ownerRequiredDoc: ArrayList<RestaurantOwnerRequiredDoc> = ArrayList<RestaurantOwnerRequiredDoc>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.restaurant_details_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RestaurantDetailsAdapter.ViewHolder, position: Int) {
        holder.title.text = ownerRequiredDoc[position].required_docs_name
        holder.titleEdit.hint = ownerRequiredDoc[position].required_docs_name
        holder.titleEdit.setText(ownerRequiredDoc[position].editText)
    }

    override fun getItemCount(): Int {
        return ownerRequiredDoc.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title : AppCompatTextView = itemView.title
        var titleEdit : AppCompatEditText = itemView.edt_info

        init {

            titleEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    ownerRequiredDoc[adapterPosition].editText = titleEdit.text.toString()
                    Log.d("adapterText", "onTextChanged: " +  ownerRequiredDoc[adapterPosition] + titleEdit.text.toString() )
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }

    }

    fun addOwnerDetails(list:ArrayList<RestaurantOwnerRequiredDoc>){
        this.ownerRequiredDoc.addAll(list)
    }
}