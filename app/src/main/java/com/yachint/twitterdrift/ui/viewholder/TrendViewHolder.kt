package com.yachint.twitterdrift.ui.viewholder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.databinding.TrendsFeedItemLayoutBinding

class TrendViewHolder(
    val binding: TrendsFeedItemLayoutBinding,
    val context: Context
): RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, trend: Trend){
        binding.trend = trend
        binding.trendRank.text = (position + 1).toString()
        if(trend.tweet_volume == null){
            binding.trendHeader.visibility = View.GONE
        }

        when(trend.state){
            "new" -> {
                binding.trendState.setBackgroundResource(R.drawable.ic_diamond_stone)
            }

            "inc" -> {
                binding.trendState.setBackgroundResource(R.drawable.ic_fire)
            }

            "dec" -> {
                binding.trendState.setBackgroundResource(R.drawable.ic_double_down)
            }
        }
    }
}