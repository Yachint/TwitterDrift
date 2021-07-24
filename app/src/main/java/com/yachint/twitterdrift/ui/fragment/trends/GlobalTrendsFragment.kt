package com.yachint.twitterdrift.ui.fragment.trends

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tencent.mmkv.MMKV
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.databinding.FragmentGlobalTrendsBinding
import com.yachint.twitterdrift.ui.activity.MainActivity
import com.yachint.twitterdrift.ui.adapter.TrendAdapter
import com.yachint.twitterdrift.ui.injector.DaggerVFMTrendsComponent
import com.yachint.twitterdrift.ui.modules.VMFTrendsModule
import com.yachint.twitterdrift.ui.viewmodel.TrendsViewModel


class GlobalTrendsFragment : Fragment() {

    lateinit var binding: FragmentGlobalTrendsBinding
    private lateinit var viewModel: TrendsViewModel
    private lateinit var trendAdapter: TrendAdapter
    private lateinit var kv: MMKV
    private var woeid: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_global_trends, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kv = (requireActivity() as MainActivity).kv

        if(kv.decodeBool("location", false)){
            woeid = kv.decodeInt("woeid")
        }

        initializeViewModel()

        trendAdapter = TrendAdapter(requireContext())
        binding.rvGlobalTrends.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trendAdapter
        }
        setUpObservers()
    }

    private fun initializeViewModel(){
        val component = DaggerVFMTrendsComponent.builder().
        vMFTrendsModule(VMFTrendsModule(requireActivity())).build()

        val factory = component.getViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory).get(TrendsViewModel::class.java)
        viewModel.getGlobalTrends()
    }

    private fun setUpObservers(){
        viewModel.globalTrendsList().observe(requireActivity(), {
            if(it.isEmpty()){
                Log.d("OBSERVER", "GlobalFragment: EMPTY ")
                if(woeid != 0){
                    viewModel.fetchGlobalTrends(20)
                }
            } else {
                Log.d("OBSERVER", "GlobalFragment: ${it.size} ")
                if(woeid == 0){
                    woeid = kv.decodeInt("woeid")
                }
                trendAdapter.submitList(it)
            }
        })
    }
}