package com.yachint.twitterdrift.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yachint.twitterdrift.data.model.trends.Trend

@Dao
interface TrendsDao {
    @Insert
    fun insertTrends(trends: List<Trend>)

    @Query("DELETE FROM trends_table")
    fun deleteTrends()

    @Query("SELECT * FROM trends_table")
    fun getTrends(): LiveData<List<Trend>>
}