package com.voila.voilasailor.restaurantRegistration.UI

import android.graphics.Color
import android.media.MediaParser
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.voila.voilasailor.R
import com.voila.voilasailor.restaurantRegistration.Adpter.TrackVerificationAdapter
import com.voila.voilasailor.restaurantRegistration.RestaurantFactory.ProfileVewModelFactory
import com.voila.voilasailor.restaurantRegistration.RestaurantFactory.RestaurantHomeViewModelFactory
import com.voila.voilasailor.restaurantRegistration.RestaurantFactory.RestaurantViewModelFactory
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.DocumentVerification
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.InformationVerification
import com.voila.voilasailor.restaurantRegistration.RestaurantModel.TrcakData
import com.voila.voilasailor.restaurantRegistration.RestaurantNetworkResponse.RestaurantVerificationTrackResponse
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.RestaurantViewModelListener
import com.voila.voilasailor.restaurantRegistration.RestaurantViewModelListner.TrackVerificationListner
import com.voila.voilasailor.restaurantRegistration.Util.toasts
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.ProfileDetailViewModel
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.RestaurantHomeViewModel
import com.voila.voilasailor.restaurantRegistration.restaurantViewModel.RestaurantRegistrationViewModel


import androidx.core.content.ContextCompat

import com.voila.voilasailor.databinding.FragmentTrackVerificationBinding
import android.app.Activity





class TrackVerificationFragment : Fragment(), TrackVerificationListner {

    lateinit var trackVerificationAdapter: TrackVerificationAdapter

    lateinit var binding: FragmentTrackVerificationBinding
    lateinit var viewmodel:RestaurantRegistrationViewModel
    var pendingPosition:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_track_verification, container, false)
        binding.track = viewmodel
        viewmodel.trackListener = this
        binding.executePendingBindings()

        val tag = requireArguments().getString("tag")
        val authToken = requireArguments().getString("auth_token")


        viewmodel.trackVerificationProcess(tag!!, authToken!!)


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel = ((activity?.run {
            ViewModelProviders.of(this,RestaurantViewModelFactory(requireContext()))[RestaurantRegistrationViewModel::class.java]
        }?:throw Exception("Invalid")) as RestaurantRegistrationViewModel?)!!

    }

    override fun MSG(string: String) {
        Toast.makeText(this.requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    override fun onTrackVerification() {
        viewmodel.restVerificationTrackObservable()
            .observe(this.requireActivity(), Observer {
                if (it!=null){
                   if (it.result){
                       trackVerification(it)
                   }
                    else{
                       MSG("Failed to track verification")
                   }
                }
                else{
                    MSG("Failed to track process")
                }
            })
    }

    private fun trackVerification(it: RestaurantVerificationTrackResponse) {
        var trac_data : ArrayList<TrcakData> = ArrayList()
        var info_list:ArrayList<InformationVerification> = ArrayList()
        trac_data = it.trcak_data as ArrayList<TrcakData>

        if (trac_data!=null && trac_data.size>0 && trac_data.isNotEmpty()){

            for (i in trac_data.indices){
                info_list= trac_data[i].InformationVerification as ArrayList<InformationVerification>
            }
            pendingPosition = info_list.size
            if (info_list!=null && info_list.isNotEmpty() && info_list.size>0) {
                var sub_doc_list :ArrayList<DocumentVerification> = ArrayList()
                for (count in info_list.indices){
                    sub_doc_list = info_list[count].DocumentVerification as ArrayList<DocumentVerification>

                }

                val activity: Activity? = activity
                if (activity != null && isAdded) {
                    trackVerificationAdapter = TrackVerificationAdapter(this.requireActivity())
                    trackVerificationAdapter.TrackVerification(info_list)
                    binding.recyclerView.adapter = trackVerificationAdapter
                    viewmodel.dismissProgressDai()
                }

            }
            else{
                viewmodel.dismissProgressDai()
                MSG("Failed to track verification")
            }
        }
        else{
            viewmodel.dismissProgressDai()
            MSG("Failed to track verification")
        }
    }

}