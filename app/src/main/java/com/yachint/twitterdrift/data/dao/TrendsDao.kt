package com.yachint.twitterdrift.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.data.model.trends.TrendStatus

@Dao
interface TrendsDao {
    @Insert
    fun insertTrends(trends: List<Trend>)

    @Query("DELETE FROM trends_table")
    fun deleteTrends()

    @Query("SELECT * FROM trends_table")
    fun getTrends(): LiveData<List<Trend>>

    @Query("SELECT * FROM trends_table WHERE type = 'global'")
    fun getGlobalTrends(): LiveData<List<Trend>>

    @Query("SELECT * FROM trends_table WHERE type = 'regional'")
    fun getRegionalTrends(): LiveData<List<Trend>>

    @Insert
    fun insertStatus(status: TrendStatus)

    @Update
    fun updateStatus(status: TrendStatus)

    @Query("SELECT hash FROM trend_status_table WHERE woeid = :woeid")
    fun getHash(woeid: Int): String
}