package com.hampson.sharework_kotlin.ui.cluster_job

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.heyalex.bottomdrawer.BottomDrawerDialog
import com.github.heyalex.bottomdrawer.BottomDrawerFragment
import com.hampson.sharework_kotlin.R
import com.hampson.sharework_kotlin.data.api.JobDBClient
import com.hampson.sharework_kotlin.data.api.JobDBInterface
import com.hampson.sharework_kotlin.data.repository.NetworkState
import com.hampson.sharework_kotlin.ui.single_job.JobRepository
import com.hampson.sharework_kotlin.ui.single_job.SingleJobViewModel

class GoogleTaskExampleDialog : BottomDrawerFragment() {

    private var alphaCancelButton = 0f
    private lateinit var cancelButton: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewError: TextView

    private lateinit var navigation: AppCompatCheckBox
    private lateinit var statusBar: AppCompatCheckBox

    private lateinit var viewModel: ClusterJobViewModel
    lateinit var jobRepository: JobPagedListRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.google_task_example_layout, container, false)
        cancelButton = view.findViewById(R.id.cancel)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        textViewError = view.findViewById(R.id.textViewError)
        val percent = 0.65f
        addBottomSheetCallback {
            onSlide { _, slideOffset ->
                val alphaTemp = (slideOffset - percent) * (1f / (1f - percent))
                alphaCancelButton = if (alphaTemp >= 0) {
                    alphaTemp
                } else {
                    0f
                }
                cancelButton.alpha = alphaCancelButton
                cancelButton.isEnabled = alphaCancelButton > 0
            }
        }
        cancelButton.setOnClickListener { dismissWithBehavior() }


        val apiService : JobDBInterface = JobDBClient.getClient()

        jobRepository = JobPagedListRepository(apiService)

        viewModel = getViewModel()

        val jobAdapter = ClusterJobPagedListAdapter((activity as FragmentActivity))

        val layout = LinearLayoutManager((activity as FragmentActivity))
        recyclerView.layoutManager = layout
        recyclerView.adapter = jobAdapter

        viewModel.jobPagedList.observe(this, Observer {
            Log.d("TESTviewModel", "START")
            jobAdapter.submitList(it)
            Log.d("TESTFviewModel", "END")
        })

        viewModel.networkState.observe(this, Observer {
            progressBar.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            textViewError.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                jobAdapter.setNetworkState(it)
            }
        })

        return view
    }

    override fun configureBottomDrawer(): BottomDrawerDialog {
        return BottomDrawerDialog.build(requireContext()) {
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("alphaCancelButton", alphaCancelButton)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        alphaCancelButton = savedInstanceState?.getFloat("alphaCancelButton") ?: 0f
        cancelButton.alpha = alphaCancelButton
        cancelButton.isEnabled = alphaCancelButton > 0
    }


    private fun getViewModel(): ClusterJobViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T{
                @Suppress("UNCHECKED_CAST")
                return ClusterJobViewModel(jobRepository) as T
            }
        }).get(ClusterJobViewModel::class.java)
    }
}
