package com.yachint.twitterdrift.ui.viewholder

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.databinding.TrendsFeedItemLayoutBinding
import com.yachint.twitterdrift.databinding.TrendsFeedModernLayoutBinding
import com.yachint.twitterdrift.ui.activity.MainActivity
import com.yachint.twitterdrift.ui.fragment.trends.GlobalTrendsFragment
import com.yachint.twitterdrift.ui.fragment.trends.RegionalTrendsFragment
import com.yachint.twitterdrift.utils.NumberFormatter

class TrendViewHolder(
    val binding: TrendsFeedModernLayoutBinding,
    val context: Context,
    val fragmentType: Int
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

        if(trend.isOpen){
            Log.d("TWEET", "bind: ${trend.isOpen}")
            binding.tweetSeparator.visibility = View.VISIBLE
            if(!trend.isTopTweetFetched){
                binding.tweetShimmer.visibility = View.VISIBLE
            } else {
                binding.tweetShimmer.visibility = View.GONE
                Log.d("TWEET", "bind: ${trend.isTopTweetFetched} - ${trend.name}")
                val tweet = (context as MainActivity).getTopTweet()
                //profile picture
                Glide.with(context).load(tweet.user.profileImage).into(binding.profilePicture)
                binding.fullName.text = tweet.user.name
                binding.username.text = tweet.user.username
                //verified
                if(tweet.user.verified){
                    binding.verifiedLogo.visibility = View.VISIBLE
                } else {
                    binding.verifiedLogo.visibility = View.GONE
                }

                //text
                binding.tweetBody.text = tweet.text

                //image
                if(tweet.media.type != "null"){
                    binding.tweetImage.visibility = View.VISIBLE
                    Glide.with(context).load(tweet.media.link).into(binding.tweetImage)
                } else {
                    binding.tweetImage.visibility = View.GONE
                }
                binding.numOfLikes.text = NumberFormatter().format(tweet.likes)
                binding.numOfRetweets.text = NumberFormatter().format(tweet.retweets)
                binding.tweetLayout.visibility = View.VISIBLE
            }
        } else {
            binding.tweetSeparator.visibility = View.GONE
            binding.tweetShimmer.visibility = View.GONE
        }

        
        binding.cardRipple.setOnClickListener {
            if(!trend.isOpen){
                trend.isOpen = true
                binding.tweetSeparator.visibility = View.VISIBLE
                binding.tweetShimmer.visibility = View.VISIBLE
                (context as MainActivity).searchForTweet(position, trend.name, fragmentType)
            } else {
                trend.isOpen = false
                binding.tweetSeparator.visibility = View.GONE
                binding.tweetShimmer.visibility = View.GONE
                if(trend.isTopTweetFetched){
                    binding.tweetLayout.visibility = View.GONE
                }
            }
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