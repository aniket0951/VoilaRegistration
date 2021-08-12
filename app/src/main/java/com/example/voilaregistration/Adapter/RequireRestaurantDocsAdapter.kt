package com.example.voilaregistration.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voilaregistration.Model.RestaurantDetailsRequiredDoc
import com.example.voilaregistration.Model.RestaurantOwnerRequiredDoc
import com.example.voilaregistration.R
import kotlinx.android.synthetic.main.all_required_docs.view.*

class RequireRestaurantDocsAdapter(val context: Context) : RecyclerView.Adapter<RequireRestaurantDocsAdapter.ViewHolder>() {

    var restaurantOwnerRequiredDocList : ArrayList<RestaurantOwnerRequiredDoc> = ArrayList<RestaurantOwnerRequiredDoc>()
    var restaurantDetailsRequiredDocList : ArrayList<RestaurantDetailsRequiredDoc> = ArrayList<RestaurantDetailsRequiredDoc>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.all_required_docs, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position < restaurantOwnerRequiredDocList.size){
            holder.docsName.text = restaurantOwnerRequiredDocList[position].required_docs_name
          //  Log.d("address", "onBindViewHolder: " + restaurantOwnerRequiredDocList[position].required_docs_name)

        }
        else{
            holder.docsName.text = restaurantDetailsRequiredDocList.get(position - restaurantOwnerRequiredDocList.size).required_docs_name
        }
    }

    override fun getItemCount(): Int {
        return  restaurantDetailsRequiredDocList.size + restaurantOwnerRequiredDocList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       val docsName: AppCompatTextView = itemView.docs_name
    }

    fun addOwnerRequiredDocs(list: List<RestaurantOwnerRequiredDoc>){
        this.restaurantOwnerRequiredDocList.addAll(list)
       // notifyDataSetChanged()
    }
    fun addRestaurantDetailsDocs(list: List<RestaurantDetailsRequiredDoc>){
       this.restaurantDetailsRequiredDocList.addAll(list)
       // notifyDataSetChanged()
    }
}