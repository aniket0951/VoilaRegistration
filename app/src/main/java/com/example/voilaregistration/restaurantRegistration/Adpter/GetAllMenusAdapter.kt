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
import com.example.voilaregistration.restaurantRegistration.RestaurantModel.Menu
import kotlinx.android.synthetic.main.menu_items_layout.view.*

class GetAllMenusAdapter(var context: Context) : RecyclerView.Adapter<GetAllMenusAdapter.ViewHolder>() {

    var menuList :ArrayList<Menu> = ArrayList()
    var filterDish : ArrayList<FilterDish> = ArrayList()
    var isFilterApply : Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GetAllMenusAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.menu_items_layout, parent, false))
    }

    override fun onBindViewHolder(holder: GetAllMenusAdapter.ViewHolder, position: Int) {

        when(menuList[position].dish_type){
            "Non Veg" -> {

                holder.menuName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_round, 0);
            }

            "Veg" -> {

                holder.menuName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round, 0);
            }
        }

        holder.menuName.text = menuList[position].dish_name
        holder.menuDescription.text = menuList[position].dish_description
        holder.menuRate.text = "\u20A8 " +  menuList[position].dish_price+"/-"

        val menuUrl = menuList[position].menuUrl
        Glide.with(context)
            .load(menuUrl)
            .into(holder.menuImage)
    }

    override fun getItemCount(): Int {
        return menuList.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuImage = itemView.menu_image
        val menuName = itemView.menu_name
        val menuDescription = itemView.menu_description
        val menuRate = itemView.menu_rate
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getAllMenuList(list: ArrayList<Menu>){
        this.menuList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun applyFilterOptions(list: ArrayList<FilterDish>){
        this.filterDish.addAll(list)
        notifyDataSetChanged()
    }
}