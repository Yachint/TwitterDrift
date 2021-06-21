package com.yachint.twitterdrift.data.repository

import androidx.lifecycle.LiveData
import com.yachint.twitterdrift.data.common.DatabaseListener
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.model.trends.Trend

interface TrendsRepository {
    fun roomInsertTrends(trends: List<Trend>, databaseListener: DatabaseListener)
    fun roomDeleteTrends()
    fun roomGetTrends(): LiveData<List<Trend>>
    fun fetchTrends(woeid: Int, repositoryListener: RepositoryListener)
    fun fetchPlaceId(lat: Double, long: Double, repositoryListener: RepositoryListener)
}