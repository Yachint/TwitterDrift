package com.yachint.twitterdrift.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.databinding.FragmentTrendsBinding
import com.yachint.twitterdrift.ui.activity.MainActivity
import com.yachint.twitterdrift.ui.adapter.viewpager.HomeViewPagerAdapter
import com.yachint.twitterdrift.ui.fragment.trends.GlobalTrendsFragment
import com.yachint.twitterdrift.ui.fragment.trends.RegionalTrendsFragment
import com.yachint.twitterdrift.ui.injector.DaggerVFMTrendsComponent
import com.yachint.twitterdrift.ui.modules.VMFTrendsModule
import com.yachint.twitterdrift.ui.viewmodel.TrendsViewModel
import com.yachint.twitterdrift.utils.PagerMotionHelper
import hari.bounceview.BounceView


class TrendsFragment : Fragment() {

    lateinit var binding: FragmentTrendsBinding
    private lateinit var viewModel: TrendsViewModel
    private var globalTrendsFragment = GlobalTrendsFragment()
    private var regionalTrendsFragment = RegionalTrendsFragment()
    private var isRefreshClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_trends, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViewModel()

        //Add support for animating header with ViewPager
        PagerMotionHelper(
            requireContext(),
            binding.expandedToolbarContent,
            binding.viewPagerTrends
        )

        binding.viewPagerTrends.adapter = HomeViewPagerAdapter(
            lifecycle = lifecycle,
            fm = requireActivity().supportFragmentManager,
            globalTrendsFragment = globalTrendsFragment,
            regionalTrendsFragment = regionalTrendsFragment
        )

        binding.regionalText.setOnClickListener {
            binding.expandedToolbarContent.transitionToStart()
            binding.viewPagerTrends.currentItem = 0
        }

        binding.globalText.setOnClickListener {
            binding.expandedToolbarContent.transitionToEnd()
            binding.viewPagerTrends.currentItem = 1
        }

        setUpObservers()

        // Bind buttons to functionalities
        BounceView.addAnimTo(binding.btnRefresh)
        binding.btnRefresh.setOnClickListener {
            (activity as MainActivity).apply {
                refreshData(1)
            }
            isRefreshClicked = true
            binding.horizontalProgressBar.visibility = View.VISIBLE
        }

        binding.btnSettings.setOnClickListener {
            (activity as MainActivity).apply {
                navigateToSettings()
            }
        }
    }

    private fun initializeViewModel(){
        val component = DaggerVFMTrendsComponent.builder()
            .vMFTrendsModule(VMFTrendsModule(requireActivity())).build()

        val factory = component.getViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory).get(TrendsViewModel::class.java)
    }

    private fun setUpObservers(){
        viewModel.getPendingRequests().observe(requireActivity(), { requestsPending ->
            if(requestsPending == 0 && isRefreshClicked){
                isRefreshClicked = false
                binding.horizontalProgressBar.visibility = View.GONE
                (activity as MainActivity).notifySuccess()
            }
        })
    }

}