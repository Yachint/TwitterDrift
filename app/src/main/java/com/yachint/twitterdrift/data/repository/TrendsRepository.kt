package com.yachint.twitterdrift.data.repository

import androidx.lifecycle.LiveData
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.model.trends.Trend

interface TrendsRepository {
    fun roomInsertTrends(trends: List<Trend>)
    fun roomDeleteTrends()
    fun roomGetTrends(): LiveData<List<Trend>>
    fun fetchTrends(repositoryListener: RepositoryListener)
}