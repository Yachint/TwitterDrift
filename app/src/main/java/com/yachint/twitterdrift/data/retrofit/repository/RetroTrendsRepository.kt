package com.yachint.twitterdrift.data.retrofit.repository

import androidx.lifecycle.LiveData
import com.yachint.twitterdrift.data.common.DatabaseListener
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.dao.TrendsDao
import com.yachint.twitterdrift.data.model.response.trends.TrendsMainResponse
import com.yachint.twitterdrift.data.model.response.woeid.PlaceMainResponse
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.data.model.trends.TrendStatus
import com.yachint.twitterdrift.data.repository.TrendsRepository
import com.yachint.twitterdrift.data.retrofit.TwitterApi
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetroTrendsRepository private constructor(
    private val trendsDao: TrendsDao
): TrendsRepository{
    private lateinit var trendsList: LiveData<List<Trend>>
    private lateinit var globalTrendList: LiveData<List<Trend>>
    private lateinit var regionalTrendList: LiveData<List<Trend>>
    private lateinit var hash: String
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    companion object {
        @Volatile private var instance: RetroTrendsRepository? = null

        fun getInstance(trendsDao: TrendsDao) =
            instance ?: synchronized(this){
                instance ?: RetroTrendsRepository(trendsDao).also {
                    instance = it
                }
            }
    }

    override fun roomInsertTrends(trends: List<Trend>, databaseListener: DatabaseListener) {
        repositoryScope.launch {
            val operation = async {
                trendsDao.insertTrends(trends)
            }

            operation.await()
            databaseListener.onQueryComplete("INSERT")
        }
    }

    override fun roomDeleteTrends() {
        repositoryScope.launch {
            val operation = async {
                trendsDao.deleteTrends()
            }

            operation.await()
        }
    }

    override fun roomGetTrends(): LiveData<List<Trend>> {
        runBlocking {
            val job = repositoryScope.launch {
                trendsList = trendsDao.getTrends()
            }

            job.join()
        }

        return trendsList
    }

    override fun fetchPlaceId(lat: Double, long: Double, repositoryListener: RepositoryListener) {
        val twitterApiService = TwitterApi.APIService

        twitterApiService?.getPlaceId(lat, long)?.enqueue(object : Callback<PlaceMainResponse>{
            override fun onResponse(
                call: Call<PlaceMainResponse>,
                response: Response<PlaceMainResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        repositoryListener.onSuccess(it.data[0])
                    }
                } else {
                    repositoryListener.onFailure(Exception(response.message()))
                }
            }

            override fun onFailure(call: Call<PlaceMainResponse>, t: Throwable) {
                repositoryListener.onFailure(t)
            }

        })
    }

    override fun fetchRegionalTrends(woeid: Int, limit: Int, repositoryListener: RepositoryListener) {
        val twitterApiService = TwitterApi.APIService

        twitterApiService?.getTrends(woeid, limit)?.enqueue(object : Callback<TrendsMainResponse> {
            override fun onResponse(
                call: Call<TrendsMainResponse>,
                response: Response<TrendsMainResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        repositoryListener.onSuccess(it)
                    }
                } else {
                    repositoryListener.onFailure(Exception(response.message()))
                }
            }

            override fun onFailure(call: Call<TrendsMainResponse>, t: Throwable) {
                repositoryListener.onFailure(t)
            }

        })
    }

    override fun fetchGlobalTrends(limit: Int, repositoryListener: RepositoryListener) {
        val twitterApiService = TwitterApi.APIService

        twitterApiService?.getTrends(1, limit)?.enqueue(object : Callback<TrendsMainResponse> {
            override fun onResponse(
                call: Call<TrendsMainResponse>,
                response: Response<TrendsMainResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        repositoryListener.onSuccess(it)
                    }
                } else {
                    repositoryListener.onFailure(Exception(response.message()))
                }
            }

            override fun onFailure(call: Call<TrendsMainResponse>, t: Throwable) {
                repositoryListener.onFailure(t)
            }

        })
    }

    override fun roomGetGlobalTrends(): LiveData<List<Trend>> {
        runBlocking {
            val job = repositoryScope.launch {
                globalTrendList = trendsDao.getGlobalTrends()
            }

            job.join()
        }

        return globalTrendList
    }

    override fun roomGetRegionalTrends(): LiveData<List<Trend>> {
        runBlocking {
            val job = repositoryScope.launch {
                regionalTrendList = trendsDao.getRegionalTrends()
            }

            job.join()
        }

        return regionalTrendList
    }

    override fun roomInsertStatus(status: TrendStatus) {
        repositoryScope.launch {
            trendsDao.insertStatus(status)
        }
    }

    override fun roomUpdateStatus(status: TrendStatus) {
        runBlocking {
            val job = repositoryScope.launch {
                trendsDao.updateStatus(status)
            }

            job.join()
        }
    }

    override fun roomGetHash(woeid: Int): String {
        runBlocking {
            val job = repositoryScope.launch {
                hash = trendsDao.getHash(woeid)
            }

            job.join()
        }
        return hash
    }
}