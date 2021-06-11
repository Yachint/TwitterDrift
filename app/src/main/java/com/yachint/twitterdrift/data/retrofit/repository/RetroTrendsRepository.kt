package com.yachint.twitterdrift.data.retrofit.repository

import androidx.lifecycle.LiveData
import com.yachint.twitterdrift.BuildConfig
import com.yachint.twitterdrift.data.common.DatabaseListener
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.dao.TrendsDao
import com.yachint.twitterdrift.data.model.response.trends.TrendsObject
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.data.repository.TrendsRepository
import com.yachint.twitterdrift.data.retrofit.TwitterApi
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetroTrendsRepository private constructor(
    private val trendsDao: TrendsDao
): TrendsRepository{
    lateinit var trendsList: LiveData<List<Trend>>
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

    override fun fetchTrends(woeid: Int, repositoryListener: RepositoryListener) {
        val twitterApiService = TwitterApi.getHeaderAPIService(BuildConfig.TWITTER_API_KEY)

        twitterApiService?.getTrends(woeid)?.enqueue(object : Callback<List<TrendsObject>>{
            override fun onResponse(
                call: Call<List<TrendsObject>>,
                response: Response<List<TrendsObject>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        repositoryListener.onSuccess(it[0])
                    }
                } else {
                    repositoryListener.onFailure(Exception(response.message()))
                }
            }

            override fun onFailure(call: Call<List<TrendsObject>>, t: Throwable) {
                repositoryListener.onFailure(t)
            }

        })
    }
}