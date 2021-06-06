package com.yachint.twitterdrift.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.model.BaseModel
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.data.retrofit.repository.RetroTrendsRepository

class TrendsViewModel(
    private val retroTrendsRepository: RetroTrendsRepository
): ViewModel(), RepositoryListener {
    private lateinit var listOfTrends: LiveData<List<Trend>>

    fun getTrendsList(): LiveData<List<Trend>>{
        listOfTrends = retroTrendsRepository.roomGetTrends()
        return listOfTrends
    }

    fun deleteTrendsList(){
        retroTrendsRepository.roomDeleteTrends()
    }

    fun fetchTrendsList(){
        retroTrendsRepository.fetchTrends(this)
    }

    override fun onSuccess(dataModel: BaseModel) {

    }

    override fun onFailure(t: Throwable) {

    }
}