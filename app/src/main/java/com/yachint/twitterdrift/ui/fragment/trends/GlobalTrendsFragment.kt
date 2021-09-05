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
import com.yachint.twitterdrift.data.model.tweet.Tweet
import com.yachint.twitterdrift.databinding.FragmentGlobalTrendsBinding
import com.yachint.twitterdrift.ui.activity.MainActivity
import com.yachint.twitterdrift.ui.adapter.TrendAdapter
import com.yachint.twitterdrift.ui.injector.DaggerVFMTrendsComponent
import com.yachint.twitterdrift.ui.injector.DaggerVMFTweetsComponent
import com.yachint.twitterdrift.ui.modules.VMFTrendsModule
import com.yachint.twitterdrift.ui.modules.VMFTweetsModule
import com.yachint.twitterdrift.ui.viewmodel.TrendsViewModel
import com.yachint.twitterdrift.ui.viewmodel.TweetsViewModel
import com.yachint.twitterdrift.utils.TrendStateMaintainer


class GlobalTrendsFragment : Fragment() {

    lateinit var binding: FragmentGlobalTrendsBinding
    private lateinit var viewModel: TrendsViewModel
    private lateinit var tweetsViewModel: TweetsViewModel
    private lateinit var trendAdapter: TrendAdapter
    private lateinit var kv: MMKV
    private var woeid: Int = 0
    var currSearchIdx: Int = -1
    var currSearchTerm: String = ""

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

        trendAdapter = TrendAdapter(requireContext(), 0)
        binding.rvGlobalTrends.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trendAdapter
        }
        setUpObservers()
    }

    private fun notifyAdapterOfUpdate(){
        trendAdapter.notifyItemChanged(currSearchIdx)
    }

    private fun initializeViewModel(){
        val component = DaggerVFMTrendsComponent.builder().
        vMFTrendsModule(VMFTrendsModule(requireActivity())).build()

        val tweetsComponent = DaggerVMFTweetsComponent.builder()
            .vMFTweetsModule(VMFTweetsModule()).build()
        val tweetsFactory = tweetsComponent.getViewModelFactory()
        tweetsViewModel = ViewModelProvider(requireActivity(), tweetsFactory).get(TweetsViewModel::class.java)

        val factory = component.getViewModelFactory()
        viewModel = ViewModelProvider(requireActivity(), factory).get(TrendsViewModel::class.java)
        viewModel.getGlobalTrends()
    }

    private fun setUpObservers(){
        viewModel.globalTrendsList().observe(requireActivity(), {
            if(it.isEmpty()){
                Log.d("OBSERVER", "GlobalFragment: EMPTY ")
                val currentTrendState = viewModel.getState(TrendStateMaintainer.GLOBAL)
                if(woeid != 0 && currentTrendState != TrendStateMaintainer.SYNCING){
                    viewModel.fetchGlobalTrends(20)
                }
            } else {
                Log.d("OBSERVER", "GlobalFragment: ${it.size} ")
                if(woeid == 0){
                    woeid = kv.decodeInt("woeid")
                }
                trendAdapter.submitList(it)
                binding.rvGlobalTrends.smoothScrollToPosition(0)
            }
        })

        tweetsViewModel.readyStatus().observe(requireActivity(), { status ->
            if(status == currSearchTerm){
                val list = trendAdapter.currentList
                list[currSearchIdx].isOpen = true;
                list[currSearchIdx].isTopTweetFetched = true;
                notifyAdapterOfUpdate()
            }
        })
    }
}