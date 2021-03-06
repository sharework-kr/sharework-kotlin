package com.hampson.sharework_kotlin.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hampson.sharework_kotlin.data.api.DBClient
import com.hampson.sharework_kotlin.data.api.DBInterface
import com.hampson.sharework_kotlin.data.repository.NetworkState
import com.hampson.sharework_kotlin.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {

    private var mBinding : FragmentNotificationBinding? = null

    private lateinit var viewModel: NotificationViewModel
    lateinit var notificationRepository: NotificationRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNotificationBinding.inflate(inflater, container, false)

        mBinding = binding

        binding.toolbar.textViewToolbarTitle.text = "알림"

        val apiService : DBInterface = DBClient.getClient(requireActivity())

        notificationRepository = NotificationRepository(apiService)

        viewModel = getViewModel()

        val adapter = NotificationPagedListAdapter(requireActivity())

        val layout = LinearLayoutManager(requireActivity())
        binding.recyclerView.layoutManager = layout
        binding.recyclerView.adapter = adapter

        viewModel.notificationPagedList.observe(requireActivity(), Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(requireActivity(), Observer {
            binding.progressBar.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.textViewError.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                adapter.setNetworkState(it)
            }
        })

        return mBinding?.root
    }

    private fun getViewModel(): NotificationViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T{
                @Suppress("UNCHECKED_CAST")
                return NotificationViewModel(notificationRepository) as T
            }
        }).get(NotificationViewModel::class.java)
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}