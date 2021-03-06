package com.hampson.sharework_kotlin.data.repository.application

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hampson.sharework_kotlin.data.api.DBInterface
import com.hampson.sharework_kotlin.data.vo.JobApplication
import io.reactivex.disposables.CompositeDisposable

class PaymentDataSourceFactory (private val apiService: DBInterface, private val compositeDisposable: CompositeDisposable,
                                private val userId: Int, private val startDate: String, private val endDate: String) : DataSource.Factory<Int, JobApplication>() {

    val applicationLiveDataSource = MutableLiveData<PaymentDataSource>()

    override fun create(): DataSource<Int, JobApplication> {
        val applicationDataSource = PaymentDataSource(apiService, compositeDisposable, userId, startDate, endDate)

        applicationLiveDataSource.postValue(applicationDataSource)
        return applicationDataSource
    }

}