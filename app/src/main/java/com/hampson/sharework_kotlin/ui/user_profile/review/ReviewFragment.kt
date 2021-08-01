package com.hampson.sharework_kotlin.ui.user_profile.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hampson.sharework_kotlin.databinding.FragmentNotificationBinding
import com.hampson.sharework_kotlin.databinding.FragmentProfileReviewBinding
import com.hampson.sharework_kotlin.ui.user_profile.ProfileViewModel

class ReviewFragment(private val viewModel: ProfileViewModel) : Fragment() {

    private var mBinding : FragmentProfileReviewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileReviewBinding.inflate(inflater, container, false)

        mBinding = binding



        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}