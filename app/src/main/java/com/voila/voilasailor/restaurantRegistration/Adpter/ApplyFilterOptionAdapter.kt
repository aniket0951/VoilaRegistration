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
import kotlinx.android.synthetic.main.menu_items_layout.view.*

class ApplyFilterOptionAdapter(var context: Context) : RecyclerView.Adapter<ApplyFilterOptionAdapter.ViewHolder>() {

     val filterDishList : ArrayList<FilterDish> = ArrayList()
    private var mListener: OnItemClickListener? = null
    private var deleteListener : OnDeleteListener? = null
    private var deletePosition : Int = 0

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

                holder.menuName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.non_veg, 0);
            }

            "Veg" -> {

                holder.menuName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.veg_img, 0);
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
        val menuImage: AppCompatImageView = itemView.menu_image
        val menuName: AppCompatTextView = itemView.menu_name
        val menuDescription: AppCompatTextView = itemView.menu_description
        val menuRate: AppCompatTextView = itemView.menu_rate
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
                    deletePosition = adapterPosition
                    if (position!=RecyclerView.NO_POSITION){
                        deleteListener!!.onDeleteClick(position)
                        Log.d("listSize", "delete position: $deletePosition")
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getFilterDish(list: ArrayList<FilterDish>){
        this.filterDishList.addAll(list)
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
        if (this.filterDishList.size !=deletePosition){

                Log.d("listSize", "removeItem before: ${filterDishList.size}" + deletePosition)
                this.filterDishList.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
                notifyItemRangeChanged(deletePosition, this.filterDishList.size)

                Log.d("listSize", "removeItem after: ${filterDishList.size}")

        }

    }
}