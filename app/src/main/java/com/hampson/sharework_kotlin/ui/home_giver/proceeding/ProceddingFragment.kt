package com.hampson.sharework_kotlin.ui.home_giver.proceeding

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hampson.sharework_kotlin.data.vo.Response
import com.hampson.sharework_kotlin.data.vo.User
import com.hampson.sharework_kotlin.databinding.FragmentNotificationBinding
import com.hampson.sharework_kotlin.databinding.FragmentProfileIntroduceBinding
import com.hampson.sharework_kotlin.databinding.FragmentRecyclerViewBinding
import com.hampson.sharework_kotlin.ui.home_giver.HomeGiverViewModel
import com.hampson.sharework_kotlin.ui.user_profile.ProfileViewModel
import org.jetbrains.anko.custom.style

class ProceddingFragment(private val viewModel: HomeGiverViewModel) : Fragment() {

    private var mBinding : FragmentRecyclerViewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)

        mBinding = binding



        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}