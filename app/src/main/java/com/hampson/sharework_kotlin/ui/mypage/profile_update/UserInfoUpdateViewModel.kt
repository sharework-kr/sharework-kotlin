package com.hampson.sharework_kotlin.ui.mypage.profile_update

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hampson.sharework_kotlin.data.api.DBInterface
import com.hampson.sharework_kotlin.data.repository.NetworkState
import com.hampson.sharework_kotlin.data.vo.Job
import com.hampson.sharework_kotlin.data.vo.JobApplication
import com.hampson.sharework_kotlin.data.vo.Response
import com.hampson.sharework_kotlin.data.vo.User
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class UserInfoUpdateViewModel (private val apiService : DBInterface) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val userLiveData: MutableLiveData<User> = MutableLiveData()
    private val updateLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val networkStateLiveData: MutableLiveData<NetworkState> = MutableLiveData()

    fun getUserInfo() : LiveData<User> {
        return userLiveData
    }

    fun getUpdateCheck() : LiveData<Boolean> {
        return updateLiveData
    }

    fun networkState() : LiveData<NetworkState> {
        return networkStateLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getUser(userId: Int) {
        networkStateLiveData.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getUser(userId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            userLiveData.postValue(it.payload.user)
                            networkStateLiveData.postValue(NetworkState.LOADED)
                        },
                        {
                            networkStateLiveData.postValue(NetworkState.ERROR)
                        }
                    )
            )
        } catch (e: Exception) {

        }
    }

    fun updateUser(userId: Int, user: User) {
        try {
            compositeDisposable.add(
                apiService.updateUser(userId, user)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            updateLiveData.postValue(true)
                        },
                        {
                            updateLiveData.postValue(false)
                        }
                    )
            )
        } catch (e: Exception) {
            updateLiveData.postValue(false)
        }
    }
}