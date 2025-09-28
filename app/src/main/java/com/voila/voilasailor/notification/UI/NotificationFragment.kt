package com.voila.voilasailor.notification.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.voila.voilasailor.R
import com.voila.voilasailor.databinding.FragmentNotificationBinding
import com.voila.voilasailor.notification.Adapter.NotificationAdapter
import com.voila.voilasailor.notification.NetworkResponse.NotificationResponse
import com.voila.voilasailor.notification.NotificationFactory.NotificationFactory
import com.voila.voilasailor.notification.NotificationListener.NotificationListener
import com.voila.voilasailor.notification.NotificationModel.Notification
import com.voila.voilasailor.notification.viewModel.NotificationViewModel

class NotificationFragment : Fragment(), NotificationListener, SwipeRefreshLayout.OnRefreshListener {

    lateinit var binding: FragmentNotificationBinding
    lateinit var viewModel: NotificationViewModel

    lateinit var notificationAdapter: NotificationAdapter
    private var mainList:ArrayList<Notification> = ArrayList()

    private var token:String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_notification, container, false)
        binding.notification = viewModel
        viewModel.listener = this
        binding.executePendingBindings()

        token = requireArguments().getString("request_token")!!

        viewModel._getNotifications(token)

        binding.swapRefresh.setOnRefreshListener(this);


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ((activity?.run {
            ViewModelProviders.of(this,
                NotificationFactory(requireContext())
            )[NotificationViewModel::class.java]
        }?:throw Exception("Invalid")) as NotificationViewModel?)!!
        setHasOptionsMenu(true)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    fun message(s:String){
        val activity: Activity? = activity
        if (activity != null && isAdded) {
            Toast.makeText(this.requireContext(), s, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNotificationSuccess() {
        viewModel.getNotificationObservable()
            .observe(this.requireActivity(), Observer {
                if (it!=null){
                    if (it.result){
                        binding.swapRefresh.isRefreshing = false
                        showNotification(it)
                    }
                    else{
                        viewModel.dismissProgressDai()
                        message("Don't have any notification.")
                    }
                }
                else{
                    viewModel.dismissProgressDai()
                    message("Don't have any notification.")
                }
            })
    }

    override fun onNotificationDeleted() {
        viewModel.deleteNotificationObservable()
            .observe(this.requireActivity(), Observer {
                if (it!=null){
                    if (it.result){
                        message(it.message)
                    }
                    else{
                        message("Failed to deleted the notification")
                    }
                }
                else{
                    message("Failed to deleted the notification")
                }
            })
    }

    private fun showNotification(it: NotificationResponse) {

        mainList = it.notifications as ArrayList<Notification>

        if (mainList.isNotEmpty() && mainList.size >0){
            val activity: Activity? = activity
            if (activity != null && isAdded) {
                notificationAdapter = NotificationAdapter(this.requireContext())
                notificationAdapter.getNotification(mainList)
                binding.recyclerView.adapter = notificationAdapter

                viewModel.dismissProgressDai()

                notificationAdapter.deleteMenuClickListener(object :NotificationAdapter.OnDeleteListener{
                    override fun onDeleteClick(position: Int) {
                       val notificationId:String = mainList[position].id.toString()
                        if (notificationId.isNotBlank() && notificationId.isNotEmpty()){
                            viewModel._removeNotification(notificationId)
                        }

                        notificationAdapter.removeItem(position)
                    }
                })
            }
        }
        else{
            viewModel.dismissProgressDai()
            message(it.message)
        }
    }

    override fun onRefresh() {
        viewModel.getNotifications(token)
    }

}