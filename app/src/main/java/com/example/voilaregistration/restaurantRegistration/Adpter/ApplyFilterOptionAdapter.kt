package com.example.voilaregistration.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.voilaregistration.R
import com.example.voilaregistration.restaurantRegistration.RestaurantModel.FilterDish
import kotlinx.android.synthetic.main.menu_items_layout.view.*

class ApplyFilterOptionAdapter(var context: Context) : RecyclerView.Adapter<ApplyFilterOptionAdapter.ViewHolder>() {

    val filterDishList : ArrayList<FilterDish> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApplyFilterOptionAdapter.ViewHolder {

        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(layoutInflater.inflate(R.layout.menu_items_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ApplyFilterOptionAdapter.ViewHolder, position: Int) {

        when(filterDishList[position].dish_type){
            "Non Veg" -> {

                holder.menuName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_round, 0);
            }

            "Veg" -> {

                holder.menuName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round, 0);
            }
        }

        holder.menuName.text = filterDishList[position].dish_name
        holder.menuDescription.text = filterDishList[position].dish_description
        holder.menuRate.text = "\u20A8 " +  filterDishList[position].dish_price+"/-"

        val menuUrl = filterDishList[position].menuUrl
        Glide.with(context)
            .load(menuUrl)
            .into(holder.menuImage)
    }

    override fun getItemCount(): Int {
        return  filterDishList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuImage = itemView.menu_image
        val menuName = itemView.menu_name
        val menuDescription = itemView.menu_description
        val menuRate = itemView.menu_rate
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getFilterDish(list: ArrayList<FilterDish>){
        this.filterDishList.addAll(list)
        notifyDataSetChanged()
    }
}