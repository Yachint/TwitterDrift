package com.yachint.twitterdrift.ui.viewholder

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.databinding.TrendsFeedItemLayoutBinding
import com.yachint.twitterdrift.databinding.TrendsFeedModernLayoutBinding
import com.yachint.twitterdrift.utils.NumberFormatter

class TrendViewHolder(
    val binding: TrendsFeedModernLayoutBinding,
    val context: Context
): RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, trend: Trend){
        binding.trend = trend
        binding.trendRank.text = (position + 1).toString()
        if(trend.tweet_volume == null){
            binding.trendHeader.visibility = View.GONE
            binding.dotSeparator.visibility = View.GONE
        } else {
            binding.trendHeader.visibility = View.VISIBLE
            binding.dotSeparator.visibility = View.VISIBLE
            binding.trendCount.text = NumberFormatter().format(trend.tweet_volume)
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