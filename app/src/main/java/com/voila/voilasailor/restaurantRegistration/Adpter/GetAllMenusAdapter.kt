package com.voila.voilasailor.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.FilterDish
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.Menu
import kotlinx.android.synthetic.main.menu_items_layout.view.*

class GetAllMenusAdapter(var context: Context) : RecyclerView.Adapter<GetAllMenusAdapter.ViewHolder>() {

    var menuList :ArrayList<Menu> = ArrayList()
    var filterDish : ArrayList<FilterDish> = ArrayList()
    private var mListener: OnItemClickListener? = null
    private var deleteListener : OnDeleteListener? = null

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

       // Log.d("MenuUrl", "onBindViewHolder: $menuUrl + ${menuList[position].dish_name}")
    }

    override fun getItemCount(): Int {
        return menuList.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val menuImage: AppCompatImageView = itemView.menu_image
        val menuName: AppCompatTextView = itemView.menu_name
        val menuDescription  : AppCompatTextView= itemView.menu_description
        val menuRate : AppCompatTextView = itemView.menu_rate
        private val editMenu : AppCompatImageView = itemView.edit_menu_details
        private val deleteMenuDetails : AppCompatImageView = itemView.delete_menu_details

        init {
            editMenu.setOnClickListener {
                if (mListener!=null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        mListener!!.onItemClick(position)
                    }
                }
            }

            deleteMenuDetails.setOnClickListener {
                if (deleteListener!=null){
                    val position = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        deleteListener!!.onDeleteClick(position)
                    }
                }
            }
        }
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

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun editMenuClickListener(listener : OnItemClickListener){

        mListener = listener
    }

    interface  OnDeleteListener{
        fun onDeleteClick(position: Int)
    }

    fun deleteMenuClickListener(listener: OnDeleteListener){
        deleteListener = listener
    }

    fun removeItem(position: Int){
        if (this.menuList.size > 0){

            if (position < menuList.size){

                Log.d("listSize", "removeItem before: ${menuList.size}")
                this.menuList.remove(this.menuList[position])
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, this.menuList.size)
                Log.d("listSize", "removeItem after: ${menuList.size}")
//                notifyItemRangeRemoved(position,this.menuList.size)
            }
        }

    }
}