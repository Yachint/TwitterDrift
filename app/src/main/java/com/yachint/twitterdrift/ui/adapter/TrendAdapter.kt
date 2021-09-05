package com.yachint.twitterdrift.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.databinding.TrendsFeedModernLayoutBinding
import com.yachint.twitterdrift.ui.viewholder.TrendViewHolder

class TrendAdapter(
    val context: Context,
    val fragmentType: Int
): ListAdapter<Trend, TrendViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Trend>(){
            override fun areItemsTheSame(oldItem: Trend, newItem: Trend): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Trend, newItem: Trend): Boolean {
                return oldItem.name == newItem.name && oldItem.ETA == newItem.ETA
                        && oldItem.state == newItem.state && oldItem.since == newItem.since
                        && oldItem.isOpen == newItem.isOpen
                        && oldItem.isTopTweetFetched == newItem.isTopTweetFetched
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendViewHolder {
        val binding = TrendsFeedModernLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return TrendViewHolder(binding, context, fragmentType)
    }

    override fun onBindViewHolder(holder: TrendViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }
}