package com.hampson.sharework_kotlin.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hampson.sharework_kotlin.data.api.DBInterface
import com.hampson.sharework_kotlin.data.vo.Job
import com.hampson.sharework_kotlin.data.vo.JobApplication
import io.reactivex.disposables.CompositeDisposable

class ApplicationDataSourceFactory (private val apiService: DBInterface, private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, JobApplication>() {

    val applicationLiveDataSource = MutableLiveData<ApplicationDataSource>()

    override fun create(): DataSource<Int, JobApplication> {
        val applicationDataSource = ApplicationDataSource(apiService, compositeDisposable)

        applicationLiveDataSource.postValue(applicationDataSource)
        return applicationDataSource
    }

}