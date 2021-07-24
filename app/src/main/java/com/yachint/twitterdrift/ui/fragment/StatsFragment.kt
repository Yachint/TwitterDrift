package com.yachint.twitterdrift.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.databinding.FragmentStatsBinding

class StatsFragment : Fragment() {

    lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stats, container, false
        )

        return binding.root
    }

}