package com.hampson.sharework_kotlin.ui.cluster_job

import android.net.Network
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hampson.sharework_kotlin.data.api.JobDBInterface
import com.hampson.sharework_kotlin.data.api.POST_PER_PAGE
import com.hampson.sharework_kotlin.data.repository.JobDataSource
import com.hampson.sharework_kotlin.data.repository.JobDataSourceFactory
import com.hampson.sharework_kotlin.data.repository.NetworkState
import com.hampson.sharework_kotlin.data.vo.Job
import io.reactivex.disposables.CompositeDisposable

class JobPagedListRepository (private val apiService : JobDBInterface) {

    lateinit var jobPagedList: LiveData<PagedList<Job>>
    lateinit var jobDataSourceFactory: JobDataSourceFactory

    fun fetchLiveJobPagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Job>> {
        jobDataSourceFactory = JobDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        Log.d("TESTFETCH", "START")
        jobPagedList = LivePagedListBuilder(jobDataSourceFactory, config).build()
        Log.d("TESTFETCH", "END")

        return jobPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<JobDataSource, NetworkState>(
            jobDataSourceFactory.jobLiveDataSource, JobDataSource::networkState
        )
    }
}