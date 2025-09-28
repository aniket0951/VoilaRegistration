package com.voila.voilasailor.notification.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.voila.voilasailor.R
import com.voila.voilasailor.notification.Adapter.NotificationAdapter.ViewHolder
import com.voila.voilasailor.notification.NotificationModel.Notification
import kotlinx.android.synthetic.main.notification_screen.view.*

class NotificationAdapter(var context: Context): RecyclerView.Adapter<ViewHolder>() {

    var notificationList: ArrayList<Notification> = ArrayList()
    private var deleteListener : OnDeleteListener? = null
    var deletePosition : Int = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.notification_screen,parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val notificationTitle:AppCompatTextView = itemView.notification_title
        private val notificationDescription:AppCompatTextView = itemView.notification_description
        private val notificationDate:AppCompatTextView = itemView.notification_date
        private val notificationDelete: AppCompatImageView = itemView.delete_notification

        init {
            notificationDelete.setOnClickListener {
                if (deleteListener!=null){
                    val position = adapterPosition
                    deletePosition = adapterPosition
                    if (position!= RecyclerView.NO_POSITION){
                        deleteListener!!.onDeleteClick(position)
                        //   Log.d("listSize", "$deletePosition: ")
                    }
                }
            }
        }

        fun bindData(position: Int){

            notificationTitle.text = notificationList[position].notification_title
            notificationDescription.text = notificationList[position].notification_description
            notificationDate.text = notificationList[position].date
        }
    }

    interface  OnDeleteListener{
        fun onDeleteClick(position: Int)
    }

    fun deleteMenuClickListener(listener: OnDeleteListener){
        deleteListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getNotification(list:ArrayList<Notification>){
        this.notificationList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int){
        if (this.notificationList.size!=deletePosition){
            //         Log.d("listSize", "removeItem before: ${menuList.size}"+deletePosition)
            this.notificationList.removeAt(deletePosition)
            notifyItemRemoved(deletePosition)
            notifyItemRangeChanged(deletePosition, this.notificationList.size)
            //        Log.d("listSize", "removeItem after: ${menuList.size}")
        }

    }

}