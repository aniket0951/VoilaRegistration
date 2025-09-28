package com.voila.voilasailor.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.DocumentVerification
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.InformationVerification
import kotlinx.android.synthetic.main.track_rest_verification.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

import android.view.animation.ScaleAnimation
import androidx.appcompat.widget.LinearLayoutCompat
import java.util.*
import kotlin.collections.ArrayList


class TrackVerificationAdapter(val context: Context): RecyclerView.Adapter<TrackVerificationAdapter.ViewHolder>() {

    var track_list:ArrayList<InformationVerification> = ArrayList<InformationVerification>()


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
        private val documentState : AppCompatTextView = itemView.document_staus
        private val parentCard : CardView = itemView.parent_card
        var recyclerView:RecyclerView = itemView.recycler_view

        init {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }

        fun bindData(position: Int){
            setFadeAnimation(itemView)
            documentName.text = track_list[position].document_name

            if (track_list[position].document_status.equals("Pending",true)){
                subDocumentName.text ="(" +track_list[position].document_status+")"
                subDocumentName.setTextColor(Color.parseColor("#FF0000"))
                parentCard.setCardBackgroundColor(Color.parseColor("#fcba03"));
                documentName.setTextColor(Color.BLACK)
            }
            else
            {
                documentState.visibility = View.GONE
                documentName.setTextColor(Color.WHITE)
                subDocumentName.text = track_list[position].document_status
                subDocumentName.setTextColor(Color.WHITE)
                parentCard.setCardBackgroundColor(Color.parseColor("#008000"))
            }

            if (track_list[position].is_sub_doc) {

                subDocumentName.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                var sub_doc_list : ArrayList<DocumentVerification> = ArrayList()
                if (track_list[position].is_sub_doc && track_list[position].DocumentVerification.isNotEmpty()) {
                    sub_doc_list = track_list[position].DocumentVerification as ArrayList<DocumentVerification>
                    val subDocAdapter: SubDocumentAdapter = SubDocumentAdapter(context)
                    subDocAdapter.getSubDocuments(sub_doc_list)
                    recyclerView.adapter = subDocAdapter
                }

            }
        }

        private fun setFadeAnimation(view: View) {

            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration = 1000
            view.startAnimation(anim)


        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun TrackVerification(track_lists:ArrayList<InformationVerification>){
        this.track_list.addAll(track_lists)
        this.notifyDataSetChanged()
    }

}