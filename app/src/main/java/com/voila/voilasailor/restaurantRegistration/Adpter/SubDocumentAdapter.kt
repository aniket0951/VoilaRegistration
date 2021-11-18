package com.voila.voilasailor.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.DocumentVerification
import kotlinx.android.synthetic.main.sub_doc_show.view.*
import kotlinx.android.synthetic.main.track_rest_verification.view.*
import kotlinx.android.synthetic.main.track_rest_verification.view.sub_document_name

class SubDocumentAdapter(val context: Context):RecyclerView.Adapter<SubDocumentAdapter.ViewHolder>() {

    var sub_doc_list : ArrayList<DocumentVerification> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubDocumentAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.sub_doc_show, parent, false))

    }

    override fun onBindViewHolder(holder: SubDocumentAdapter.ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
       return sub_doc_list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val documentName :AppCompatTextView = itemView.sub_document_name
        private val documentState: AppCompatTextView = itemView.sub_document_status


        fun bindData(position: Int){
            if (sub_doc_list.size >0) {
                documentName.text = sub_doc_list[position].document_name
                if (sub_doc_list[position].document_status.equals("Pending", true)) {
                    documentState.text = "(" + sub_doc_list[position].document_status + ")"
                    documentState.setTextColor(Color.parseColor("#FF0000"))
                    documentName.setTextColor(Color.BLACK)
                } else {
                    documentName.setTextColor(Color.WHITE)
                    documentState.text = "(" + sub_doc_list[position].document_status + ")"
                    documentState.setTextColor(Color.parseColor("#FFFFFF"))
                }
            }
        }


    }


    @SuppressLint("NotifyDataSetChanged")
    fun getSubDocuments(list:ArrayList<DocumentVerification>){
        this.sub_doc_list.addAll(list)
        notifyDataSetChanged()
    }

}