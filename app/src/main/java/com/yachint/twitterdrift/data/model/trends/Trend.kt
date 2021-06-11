package com.yachint.twitterdrift.data.model.trends

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yachint.twitterdrift.data.model.BaseModel

@Entity(tableName = "trends_table")
data class Trend(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    val url: String,
    val tweet_volume: Long
): BaseModel