package com.yachint.twitterdrift.data.retrofit.repository

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.dao.TrendsDao
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.data.repository.TrendsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RetroTrendsRepository private constructor(
    private val trendsDao: TrendsDao
): TrendsRepository{
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

    override fun roomInsertTrends(trends: List<Trend>) {
        repositoryScope.launch {
            val operation = async {
                trendsDao.insertTrends(trends)
            }

            operation.await()
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

    override fun roomGetTrends(): LiveData<List<Trend>> =
        liveData {
            trendsDao.getTrends()
        }

    override fun fetchTrends(repositoryListener: RepositoryListener) {
        Handler(Looper.getMainLooper()).postDelayed({
            //API Call
        }, 1000)
    }
}