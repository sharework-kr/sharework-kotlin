package com.hampson.sharework_kotlin.ui.management_user.sign_up

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hampson.sharework_kotlin.R
import com.hampson.sharework_kotlin.data.api.DBInterface
import com.hampson.sharework_kotlin.data.vo.User
import com.hampson.sharework_kotlin.session.SessionManagement
import com.hampson.sharework_kotlin.ui.WorkerMainActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class SignUpViewModel (private val apiService : DBInterface, application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    private val mAction: MutableLiveData<Any> = MutableLiveData()
    private val mToast: MutableLiveData<String> = MutableLiveData()

    private val context = getApplication<Application>().applicationContext

    fun getAction(): LiveData<Any> {
        return mAction
    }

    fun getToast(): LiveData<String> {
        return mToast
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun createUser(user: User) {
        try {
            compositeDisposable.add(
                apiService.createUser(user)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            if (it.status == "success") {
                                saveSession(it.payload.user)
                                moveToActivity(WorkerMainActivity::class.java)
                            } else {

                            }
                        },
                        {

                        }
                    )
            )
        } catch (e: Exception) {

        }
    }

    private fun saveSession(it: User) {
        val id = it.id
        val phoneNumber = it.phone
        val email = it.email
        var appType: String = context.getString(R.string.worker)

        val user = User(id, null, appType, null, null, null, null,
            null, null, null, null, null)

        val sessionManagement = SessionManagement(context)
        sessionManagement.saveSession(user)
    }

    private fun moveToActivity(activity: Any) {
        mAction.postValue(activity)
    }

    private fun popupToToast(message: String) {
        mToast.postValue(message)
    }
}