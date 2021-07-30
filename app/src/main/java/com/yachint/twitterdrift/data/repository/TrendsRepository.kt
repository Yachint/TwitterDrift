package com.yachint.twitterdrift.data.repository

import androidx.lifecycle.LiveData
import com.yachint.twitterdrift.data.common.DatabaseListener
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.data.model.trends.TrendStatus

interface TrendsRepository {
    fun roomInsertTrends(trends: List<Trend>, databaseListener: DatabaseListener)
    fun roomDeleteTrends()
    fun roomGetTrends(): LiveData<List<Trend>>
    fun fetchPlaceId(lat: Double, long: Double, repositoryListener: RepositoryListener)
    fun fetchRegionalTrends(woeid: Int, limit: Int, repositoryListener: RepositoryListener)
    fun fetchGlobalTrends(limit: Int, repositoryListener: RepositoryListener)
    fun roomGetGlobalTrends(): LiveData<List<Trend>>
    fun roomGetRegionalTrends(): LiveData<List<Trend>>
    fun roomInsertStatus(status: TrendStatus)
    fun roomUpdateStatus(status: TrendStatus)
    fun roomGetHash(woeid: Int): String
}