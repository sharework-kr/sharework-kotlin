package com.hampson.sharework_kotlin.ui.management_user.authentication_phone_number

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hampson.sharework_kotlin.R
import com.hampson.sharework_kotlin.data.api.DBInterface
import com.hampson.sharework_kotlin.data.repository.NetworkState
import com.hampson.sharework_kotlin.data.vo.Response
import com.hampson.sharework_kotlin.data.vo.SmsAuth
import com.hampson.sharework_kotlin.data.vo.User
import com.hampson.sharework_kotlin.session.SessionManagement
import com.hampson.sharework_kotlin.ui.MainActivity
import com.hampson.sharework_kotlin.ui.management_user.sign_up.SignUpActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class AuthenticationPhoneNumberViewModel (private val authenticationPhoneNumberRepository : AuthenticationPhoneNumberRepository,
                                          private val apiService : DBInterface, application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    private val liveData: MutableLiveData<SmsAuth> = MutableLiveData()
    private val mAction: MutableLiveData<Any> = MutableLiveData()
    private val mToast: MutableLiveData<String> = MutableLiveData()

    private val context = getApplication<Application>().applicationContext

    fun getSmsAuth(): LiveData<SmsAuth> {
        return liveData
    }

    fun getAction(): LiveData<Any> {
        return mAction
    }

    fun getToast(): LiveData<String> {
        return mToast
    }

    val networkState : LiveData<NetworkState> by lazy {
        authenticationPhoneNumberRepository.getAuthenticationPhoneNumberNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun sendPhoneNumber(phoneNumber: String) {
        try {
            compositeDisposable.add(
                apiService.sendPhoneNumber(phoneNumber)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            liveData.postValue(it.payload.smsAuth)
                        },
                        {

                        }
                    )
            )
        } catch (e: Exception) {

        }
    }

    fun sendVerifiedNumber(phoneNumber: String, token: String, verifiedNumber: String) {
        try {
            compositeDisposable.add(
                apiService.sendVerifiedNumber(phoneNumber, token, verifiedNumber)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            if (it.status == "success") {
                                if (it.payload.meta.is_already_auth_phone_number) {
                                    saveSession(it)
                                    moveToActivity(MainActivity::class.java)
                                } else {
                                    isNotPhoneNumber()
                                }
                            } else {

                            }
                        },
                        {
                            popupToToast("인증번호가 유효하지 않습니다.")
                        }
                    )
            )
        } catch (e: Exception) {

        }
    }

    private fun saveSession(it: Response) {
        val id = it.payload.smsAuth.user.id
        val phoneNumber = it.payload.smsAuth.user.phone
        val email = it.payload.smsAuth.user.email
        val get_app_type = it.payload.smsAuth.user.app_type

        var app_type: String = context.getString(R.string.worker)
        if (get_app_type == "0") {
            app_type = context.getString(R.string.giver)
        } else if (get_app_type == "1") {
            app_type = context.getString(R.string.worker)
        }

        val user = User(id, phoneNumber, app_type, email, null, null, null, null,
            null, null, null, null)

        val sessionManagement = SessionManagement(context)
        sessionManagement.saveSession(user)
    }

    private fun isNotPhoneNumber() {
        moveToActivity(SignUpActivity::class.java)
    }

    private fun moveToActivity(activity: Any) {
        mAction.postValue(activity)
    }

    private fun popupToToast(message: String) {
        mToast.postValue(message)
    }
}