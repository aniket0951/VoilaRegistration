package com.voila.voilasailor.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.voila.voilasailor.Model.DriverKycRequiredDoc
import com.voila.voilasailor.Model.DriverVehicleRequiredDoc
import com.voila.voilasailor.R
import kotlinx.android.synthetic.main.all_required_docs.view.*

@Suppress("UNREACHABLE_CODE")
class RequiredDocsAdapter(val context: Context) : RecyclerView.Adapter<RequiredDocsAdapter.ViewHolder>() {

    var kycList: ArrayList<DriverKycRequiredDoc> = ArrayList<DriverKycRequiredDoc>()
    var vehicleList: ArrayList<DriverVehicleRequiredDoc> = ArrayList<DriverVehicleRequiredDoc>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequiredDocsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.all_required_docs, parent, false))
    }

    override fun onBindViewHolder(holder: RequiredDocsAdapter.ViewHolder, position: Int) {

        if (position < kycList.size){
            holder.docsName.text = kycList[position].required_docs_name
           // Log.d("address", "onBindViewHolder: " + kycList[position].required_docs_name)

        }
        else{
           holder.docsName.text = vehicleList.get(position - kycList.size).required_docs_name
        }
    }

    override fun getItemCount(): Int {

        return kycList.size + vehicleList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val docsName: AppCompatTextView = itemView.docs_name
        val vehiclDocsName : AppCompatTextView = itemView.vehicle_docs_name
    }

    fun getKYCList(list: ArrayList<DriverKycRequiredDoc>){
        this.kycList.addAll(list)
       // notifyDataSetChanged()
       // notifyItemInserted(kycList.size)
    }

    fun getVehicle(list: ArrayList<DriverVehicleRequiredDoc>){
        this.vehicleList.addAll(list)
       // notifyDataSetChanged()
      //  notifyItemInserted(vehicleList.size)
    }


}