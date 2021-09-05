package com.yachint.twitterdrift.data.model.trends

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yachint.twitterdrift.data.model.BaseModel
import com.yachint.twitterdrift.data.model.tweet.Tweet

@Entity(tableName = "trends_table")
data class Trend(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String,
    val url: String,
    val ETA: Long,
    val state: String,
    val since: String,
    val tweet_volume: Long? = null,
    val type: String,
    var isOpen: Boolean = false,
    var isTopTweetFetched: Boolean = false
): BaseModel