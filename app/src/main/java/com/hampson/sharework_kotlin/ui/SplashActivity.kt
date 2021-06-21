package com.hampson.sharework_kotlin.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hampson.sharework_kotlin.databinding.ActivitySplashBinding
import com.hampson.sharework_kotlin.ui.management_user.AuthenticationPhoneNumberActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        Handler().postDelayed({
            val intent = Intent(this, AuthenticationPhoneNumberActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}