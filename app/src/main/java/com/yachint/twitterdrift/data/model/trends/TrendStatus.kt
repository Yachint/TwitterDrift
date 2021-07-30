package com.yachint.twitterdrift.data.model.trends

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yachint.twitterdrift.data.model.BaseModel

@Entity (tableName = "trend_status_table")
data class TrendStatus(
    @PrimaryKey val woeid: Int,
    val hash: String
): BaseModel