package com.voila.voilasailor.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.DocumentVerification
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.InformationVerification
import kotlinx.android.synthetic.main.track_rest_verification.view.*

class TrackVerificationAdapter(val context: Context): RecyclerView.Adapter<TrackVerificationAdapter.ViewHolder>() {

    var track_list:ArrayList<InformationVerification> = ArrayList<InformationVerification>()
    var sub_doc_list:ArrayList<DocumentVerification> = ArrayList<DocumentVerification>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackVerificationAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.track_rest_verification, parent, false))

    }

    override fun onBindViewHolder(holder: TrackVerificationAdapter.ViewHolder, position: Int) {
        holder.bindData(position)

    }

    override fun getItemCount(): Int {
       return track_list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val documentName: AppCompatTextView = itemView.document_name
        private val subDocumentName : AppCompatTextView = itemView.sub_document_name
        val documentState : AppCompatTextView = itemView.document_staus
        var recyclerView:RecyclerView = itemView.recycler_view


        init {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        fun bindData(position: Int){
            documentName.text = track_list[position].document_name

            if (track_list[position].document_status.equals("Pending",true)){
                documentState.text ="(" +track_list[position].document_status+")"
                documentState.setTextColor(Color.parseColor("#FF0000"))
                documentState.visibility = View.VISIBLE
            }
            else{
                documentState.visibility = View.GONE
                subDocumentName.text = track_list[position].document_status
            }

            if (track_list[position].is_sub_doc){

                subDocumentName.visibility = View.GONE

                sub_doc_list = track_list[position].DocumentVerification as ArrayList<DocumentVerification>
                val subDocAdapter:SubDocumentAdapter = SubDocumentAdapter(context)
                subDocAdapter.getSubDocuments(sub_doc_list)
                recyclerView.adapter = subDocAdapter

            }

        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun TrackVerification(track_lists:ArrayList<InformationVerification>, sub_doc:ArrayList<DocumentVerification>){
        this.track_list.addAll(track_lists)
        this.sub_doc_list.addAll(sub_doc)
        this.notifyDataSetChanged()
    }
}