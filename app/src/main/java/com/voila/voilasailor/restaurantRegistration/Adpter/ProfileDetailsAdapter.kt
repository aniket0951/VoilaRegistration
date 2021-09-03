package com.voila.voilasailor.restaurantRegistration.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.OwnerInfo
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.RestaurantInfo
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.RestaurantProfilePic
import kotlinx.android.synthetic.main.owne_details_layout.view.*

class ProfileDetailsAdapter(var context: Context) : RecyclerView.Adapter<ProfileDetailsAdapter.ViewHolder>() {

    var ownerDetailsList : ArrayList<OwnerInfo> = ArrayList()
    var restaurantDetailsList : ArrayList<RestaurantInfo> = ArrayList()
    var restaurantDocsList : ArrayList<RestaurantProfilePic> = ArrayList()

    private var isOwnerDetailsShow  : Boolean = false
    var isRestaurantDetailsShow : Boolean = false
    var isRestaurantDocsShow : Boolean = false

    private var mListener: OnItemClickListener? = null
    private var outdoorListener  : OnOutdoorClickListener? = null
    private var licenceListener : OnLicenceClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileDetailsAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return ViewHolder(layoutInflater.inflate(R.layout.owne_details_layout,parent,false))

    }

    override fun onBindViewHolder(holder: ProfileDetailsAdapter.ViewHolder, position: Int) {

        when {
            isOwnerDetailsShow -> {
                holder.ownerDetailParentLayout.visibility = View.VISIBLE
                holder.restaurantProfileParentLayout.visibility = View.GONE
                holder.restaurantDocsParentLayout.visibility = View.GONE

                holder.ownerNameEdit.setText(ownerDetailsList[position].owner_name)
                holder.ownerEmailEdit.setText(ownerDetailsList[position].owner_email)
                holder.ownerContactNoEdit.setText(ownerDetailsList[position].owner_contact_no)
                holder.ownerCurrentAddressEdit.setText(ownerDetailsList[position].owner_current_address)
                holder.ownerPermanentAddressEdit.setText(ownerDetailsList[position].owner_permanent_address)
            }
            isRestaurantDetailsShow -> {
                holder.ownerDetailParentLayout.visibility = View.GONE
                holder.restaurantProfileParentLayout.visibility = View.VISIBLE
                holder.restaurantDocsParentLayout.visibility = View.GONE

                holder.restaurantNameEdit.setText(restaurantDetailsList[position].restaurant_name)
                holder.restaurantEmailEdit.setText(restaurantDetailsList[position].restaurant_email)
                holder.restaurantContactNoEdit.setText(restaurantDetailsList[position].restaurant_contact_no)
                holder.restaurantOpeningTimeEdit.setText(restaurantDetailsList[position].restaurant_opening_time)
                holder.restaurantClosingTimeEdit.setText(restaurantDetailsList[position].restaurant_close_time)
                holder.restaurantWebsiteEdit.setText(restaurantDetailsList[position].restaurant_website)
                holder.restaurantEstablishmentYearEdit.setText(restaurantDetailsList[position].restaurant_establishment_year)
                holder.restaurantCuisinesTypeEdit.setText(restaurantDetailsList[position].restaurant_cuisines_type)

            }
            isRestaurantDocsShow -> {
                holder.ownerDetailParentLayout.visibility = View.GONE
                holder.restaurantProfileParentLayout.visibility = View.GONE
                holder.restaurantDocsParentLayout.visibility = View.VISIBLE

                val indoorPhoto : String = restaurantDocsList[position].restaurant_indoor_photo
                val outdoorPhoto : String = restaurantDocsList[position].restaurant_outdoor_photo
                val licencePhoto : String = restaurantDocsList[position].restaurant_licence_photo

                Glide.with(context).load(indoorPhoto).into(holder.restaurantIndoorImage)
                Glide.with(context).load(outdoorPhoto).into(holder.restaurantOutdoorImage)
                Glide.with(context).load(licencePhoto).into(holder.restaurantLicenceImage)
            }
        }
    }

    override fun getItemCount(): Int {
        return when {
            isOwnerDetailsShow -> {
                ownerDetailsList.size
            }
            isRestaurantDetailsShow -> {
                restaurantDetailsList.size
            }
            isRestaurantDocsShow -> {
                restaurantDocsList.size
            }
            else -> 0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        /*--- parent layout to view visible and invisible --*/
        val ownerDetailParentLayout :LinearLayoutCompat = itemView.owner_details_parent_layout
        val restaurantProfileParentLayout: LinearLayoutCompat = itemView.restaurant_details_parent_layout
        val restaurantDocsParentLayout : LinearLayoutCompat = itemView.restaurant_docs_parent_payout

        /*---- owner edit ----*/
        val ownerNameEdit : AppCompatEditText = itemView.owner_name_edt_info
        val ownerEmailEdit : AppCompatEditText = itemView.owner_email_edt_info
        val ownerContactNoEdit : AppCompatEditText = itemView.owner_contact_edt_info
        val ownerCurrentAddressEdit : AppCompatEditText = itemView.owner_current_address_edt_info
        val ownerPermanentAddressEdit : AppCompatEditText = itemView.owner_permant_address_edt_info

        /*--- Restaurant edit ------ */
        val restaurantNameEdit : AppCompatEditText = itemView.restaurant_name_edt_info
        val restaurantEmailEdit : AppCompatEditText = itemView.restaurant_email_edt_info
        val restaurantContactNoEdit : AppCompatEditText = itemView.restaurant_contact_no_edt_info
        val restaurantOpeningTimeEdit : AppCompatEditText = itemView.restaurant_opening_time_edt_info
        val restaurantClosingTimeEdit : AppCompatEditText = itemView.restaurant_close_time_edt_info
        val restaurantWebsiteEdit : AppCompatEditText = itemView.restaurant_website_edt_info
        val restaurantEstablishmentYearEdit : AppCompatEditText = itemView.restaurant_establishment_year_edt_info
        val restaurantCuisinesTypeEdit : AppCompatEditText = itemView.restaurant_cuisines_type_edt_info

        /*------ Restaurant docs ----- */
        val restaurantIndoorImage : AppCompatImageView = itemView.restaurant_indoor_photo
        val restaurantOutdoorImage : AppCompatImageView = itemView.restaurant_outdoor_photo
        val restaurantLicenceImage : AppCompatImageView = itemView.restaurant_licence_photo

        init {

            //owner details
            if (isOwnerDetailsShow){
                ownerNameEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        ownerDetailsList[adapterPosition].ownerName = ownerNameEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
                ownerEmailEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        ownerDetailsList[adapterPosition].ownerEmail = ownerEmailEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
                ownerContactNoEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        ownerDetailsList[adapterPosition].ownerContactNo = ownerContactNoEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
                ownerCurrentAddressEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        ownerDetailsList[adapterPosition].ownerCurrentAddress = ownerCurrentAddressEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
                ownerPermanentAddressEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        ownerDetailsList[adapterPosition].ownerPermanentAddress = ownerPermanentAddressEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            }


            /*--- Restaurant Details ----*/

            restaurantNameEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantName = restaurantNameEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                })
            restaurantEmailEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantEmail = restaurantEmailEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantContactNoEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantContactNo = restaurantContactNoEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantOpeningTimeEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantOpeningTime = restaurantOpeningTimeEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantClosingTimeEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantClosingTime = restaurantClosingTimeEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantWebsiteEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantWebsite = restaurantWebsiteEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantEstablishmentYearEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantEstablishmentYear = restaurantEstablishmentYearEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })
            restaurantCuisinesTypeEdit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        restaurantDetailsList[adapterPosition].restaurantCuisinesType = restaurantCuisinesTypeEdit.text.toString()
                    }

                    override fun afterTextChanged(p0: Editable?) {

                    }
                })

            restaurantIndoorImage.setOnClickListener {
                if (mListener!=null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        mListener!!.onItemClick(position)
                    }
                }
            }

            restaurantOutdoorImage.setOnClickListener {
                if (outdoorListener!=null){
                    val position = adapterPosition
                    if (position!= RecyclerView.NO_POSITION){
                        outdoorListener!!.onOutDoorItemClick(position)
                    }
                }
            }

            restaurantLicenceImage.setOnClickListener {
                if (licenceListener!=null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        licenceListener!!.onLicenceItemClick(position)
                    }
                }
            }
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    fun showOwnerDetails(list:ArrayList<OwnerInfo>){
        this.ownerDetailsList.addAll(list)
        this.isOwnerDetailsShow = true
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showRestaurantDetails(list:ArrayList<RestaurantInfo>){
        this.restaurantDetailsList.addAll(list)
        this.isRestaurantDetailsShow = true
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showRestaurantDocsDetails(list:ArrayList<RestaurantProfilePic>){
        this.restaurantDocsList.addAll(list)
        this.isRestaurantDocsShow = true
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun restaurantIndoorClickListener(listener : OnItemClickListener){

        mListener = listener
    }

    interface OnOutdoorClickListener{
        fun onOutDoorItemClick(position: Int)
    }

    fun restaurantOutdoorClickListener(listener : OnOutdoorClickListener){
        outdoorListener = listener
    }

    interface OnLicenceClickListener{
        fun onLicenceItemClick(position: Int)
    }

    fun restaurantLicenceClickListener(listener: OnLicenceClickListener){
        licenceListener = listener
    }
}