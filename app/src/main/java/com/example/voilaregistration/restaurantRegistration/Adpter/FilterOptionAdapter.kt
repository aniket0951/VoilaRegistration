package com.example.voilaregistration.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.voilaregistration.R
import com.example.voilaregistration.restaurantRegistration.RestaurantModel.FilterOption
import kotlinx.android.synthetic.main.filter_option_text_layout.view.*

class FilterOptionAdapter(var context: Context) : RecyclerView.Adapter<FilterOptionAdapter.ViewHolder>() {

    var filterOptionList : ArrayList<FilterOption> = ArrayList()
    private var mListener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterOptionAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.filter_option_text_layout,parent,false))
    }

    override fun onBindViewHolder(holder: FilterOptionAdapter.ViewHolder, position: Int) {

        holder.optionName.text = filterOptionList[position].filter_name
    }

    override fun getItemCount(): Int {
       return filterOptionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val optionName = itemView.option_name

        init {
            optionName.setOnClickListener {
                if (mListener!=null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        mListener!!.onItemClick(position)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addFilterOptions(list: ArrayList<FilterOption>){
        this.filterOptionList.addAll(list)
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : OnItemClickListener){

        mListener = listener
    }
}