package com.voila.voilasailor.driverRegistration

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.voila.voilasailor.Helper.Helper
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.FragmentRateCardBinding
import com.voila.voilasailor.driverRegistration.Model.MaxRate
import com.voila.voilasailor.driverRegistration.Model.MinRate
import com.voila.voilasailor.driverRegistration.NetworkResponse.CustomRateCardResponse
import com.voila.voilasailor.driverRegistration.NetworkResponse.SystemRateCardResponse
import com.voila.voilasailor.driverRegistration.ViewModelFactory.RateCardFactory
import com.voila.voilasailor.driverRegistration.ViewModelListener.RateCardViewModelListener
import com.voila.voilasailor.driverRegistration.viewModel.RateCardViewModel

class RateCardFragment : Fragment(), RateCardViewModelListener {

    lateinit var binding : FragmentRateCardBinding
    lateinit var viewModel : RateCardViewModel
    var isEnable:Boolean = false
    var isCustomRateEnable:Boolean = false
    var isRateChange:ObservableField<Boolean> = ObservableField(false)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_rate_card,container,false)
        binding.ratecard = viewModel
        viewModel.listener = this
        val sharedPreferences = this.requireContext().getSharedPreferences("voila", Context.MODE_PRIVATE)
        val IsEnable = sharedPreferences.getString("IsEnable","")
        if (IsEnable == "systemRateCard"){
           viewModel.useSystemRateCard()
            isEnable = true
        }
        else if (IsEnable == "customRateCard"){
            viewModel.useCustomRateCard()
            isCustomRateEnable = true
            binding.btnConformSystemRate.visibility = View.GONE
        }
        return binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this,
                RateCardFactory(requireContext())
            )[RateCardViewModel::class.java]


        } ?: throw Exception("Invalid Activity")
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.rate_card)
        if (item != null) item.isVisible = false
    }

    override fun onSystemRateSelect() {
        viewModel.getSystemRateCardObservable()
            .observe(this, Observer {
                if (it!=null){
                    getSystemRateCard(it)
                }
                else{
                    viewModel.dismissDialog()
                    Helper.onFailedMSG.onFailed(this.requireContext(), "Oops currently system rate card not available...")
                }
            })

    }

    private fun getSystemRateCard(it: SystemRateCardResponse) {
        if (it.result){
            viewModel.dismissDialog()

           val systemList = it.systemRates
           var minRateList: ArrayList<MinRate> = ArrayList()
            var maxRateList: ArrayList<MaxRate> = ArrayList()
            var min_rate : String = ""
            var max_rate : String = ""

            for (items in systemList.indices){
                val vehicle_type = systemList[items].vehicle_type
                val vehicle_number = systemList[items].vehicle_RTO_registration_number

                binding.vehicleTypeEdt.setText(vehicle_type)
                binding.vehicleNumberEdt.setText(vehicle_number)

                 minRateList = systemList[items].min_rate as ArrayList<MinRate>
                 maxRateList = systemList[items].max_rate as ArrayList<MaxRate>
            }

            for (rates in minRateList.indices){
                min_rate = minRateList[rates].min_rate
                binding.minimumRateEdt.setText("\u20B9$min_rate/km")
            }

            for (count in maxRateList.indices){
                max_rate = maxRateList[count].max_rate
                binding.maximumRateEdt.setText("\u20B9$max_rate/km")
            }

            binding.btnConformSystemRate.setOnClickListener {
                viewModel.conformRateCard(min_rate,max_rate)
            }

            disableChoiceCard()
            showSystemRateCardParent()
        }
        else{
            viewModel.dismissDialog()
            Helper.onFailedMSG.onFailed(this.requireContext(), it.message)
        }
    }

    override fun onCustomRateSelect() {
        viewModel.customRateCardObservable()
            .observe(this.requireActivity(), Observer {
                if (it!=null){
                    if (it.result){
                        showCustomRateCard(it)
                    }
                    else{
                        viewModel.dismissDialog()
                        Helper.onFailedMSG.onFailed(this.requireContext(),it.message)
                    }
                }
                else{
                    viewModel.dismissDialog()
                    Toast.makeText(this.requireContext(), "Failed to use a custom rate card please try again", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun showCustomRateCard(it: CustomRateCardResponse) {
        if (it.isRateCardSet){
            val vehicleDetails = it.vehicleInfoRate
            val oldRates = it.oldRates

            var minRate:String = ""
            var maxRate:String = ""

            for (count in vehicleDetails.indices){
                val vehicleType = vehicleDetails[count].vehicle_type
                val vehicleNumber = vehicleDetails[count].vehicle_RTO_registration_number

                binding.conformVehicleTypeEdt.setText(vehicleType)
                binding.conformVehicleNumberEdt.setText(vehicleNumber)
            }

            for (count in oldRates.indices){
                minRate = oldRates[count].min_rate.toString()
                binding.conformMinimumRateEdt.hint = ("\u20B9$minRate/km")
            }

            for (count in oldRates.indices){
                maxRate = oldRates[count].max_rate.toString()
                binding.conformMaximumRateEdt.hint = ("\u20B9$maxRate/km")
            }

            binding.conformMinimumRateEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
                override fun afterTextChanged(p0: Editable?) {
                    isRateChange.set(true)
                    minRate = binding.conformMinimumRateEdt.text.toString()
                    showCustomRateCardParent()
                }
            })

            binding.conformMaximumRateEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    isRateChange.set(true)
                    maxRate = binding.conformMaximumRateEdt.text.toString()
                    showCustomRateCardParent()
                }
            })

            binding.btnConformCustomRate.setOnClickListener {
                viewModel.conformCustomRateCard(minRate,maxRate)
            }

            disableChoiceCard()
            disableSystemRateCardParent()
            showCustomRateCardParent()
            viewModel.dismissDialog()
        }
    }

    override fun onConformRateCard() {
        viewModel.conformRateCardObservable()
            .observe(this.requireActivity(), Observer {
                if (it!=null){
                    if (it.result){
                        val activity: Activity? = activity
                        viewModel.insertPrefOfRateCard("systemRateCard")
                        if (activity!=null && isAdded) {
                            viewModel.dismissDialog()
                            Helper.onSuccessMSG.onSuccess(requireActivity().applicationContext, it.message)
                        }
                    }
                    else {
                        if (activity!=null && isAdded) {
                            viewModel.dismissDialog()
                            Helper.onSuccessMSG.onSuccess(requireActivity().applicationContext, it.message)
                        }
                    }
                }
                else{
                    viewModel.dismissDialog()
                    Helper.onFailedMSG.onFailed(requireActivity().applicationContext, "Failed to conform the rate card please try again")
                }
            })
    }

    override fun onChangeRateCardType() {
        disableSystemRateCardParent()
        disableCustomRateCardParent()
        showChoiceCard()
    }

    override fun onConformCustomRateCard() {
        viewModel.conformRateCardObservable()
            .observe(this.requireActivity(), Observer {
                if (it!=null){
                    if (it.result){
                        viewModel.insertPrefOfRateCard("customRateCard")
                        viewModel.dismissDialog()
                        Helper.onSuccessMSG.onSuccess(this.requireContext(), it.message)
                    }
                    else{
                        viewModel.dismissDialog()
                        Helper.onFailedMSG.onFailed(this.requireContext(), it.message)
                    }
                }
                else{
                    viewModel.dismissDialog()
                    Helper.onFailedMSG.onFailed(this.requireContext(), "Failed to conform the rate card please try again")
                }
            })
    }

    private fun disableChoiceCard(){binding.choiceRateCardParent.visibility = View.GONE}
    private fun disableSystemRateCardParent(){ binding.systemRateCardParent.visibility = View.GONE }
    private fun disableCustomRateCardParent(){binding.conformCustomRateParent.visibility = View.GONE}

    private fun showChoiceCard(){binding.choiceRateCardParent.visibility = View.VISIBLE}
    private fun showSystemRateCardParent(){
        binding.systemRateCardParent.visibility = View.VISIBLE
        when {
            isEnable -> {
                binding.btnConformSystemRate.visibility = View.GONE
                binding.btnChangeRateCard.visibility = View.VISIBLE
            }
            isCustomRateEnable -> {
                binding.btnConformSystemRate.visibility = View.VISIBLE
                binding.btnChangeRateCard.visibility = View.GONE
            }
            else -> {
                binding.btnConformSystemRate.visibility = View.VISIBLE
                binding.btnChangeRateCard.visibility = View.GONE
            }
        }
    }
    private fun showCustomRateCardParent(){
        binding.conformCustomRateParent.visibility = View.VISIBLE
        if(this.isRateChange.get() == true){
            binding.btnConformCustomRate.visibility = View.VISIBLE
            binding.btnCustomChangeRateCard.visibility = View.GONE
            Log.d("isRateCardChange", "showCustomRateCardParent: true")
        }
        else if (this.isRateChange.get() == false){
            Log.d("isRateCardChange", "showCustomRateCardParent: false")
            binding.btnConformCustomRate.visibility = View.GONE
            binding.btnCustomChangeRateCard.visibility = View.VISIBLE
        }
    }
}