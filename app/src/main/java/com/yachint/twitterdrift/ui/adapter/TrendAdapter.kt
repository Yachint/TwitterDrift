package com.yachint.twitterdrift.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.yachint.twitterdrift.data.model.trends.Trend
import com.yachint.twitterdrift.databinding.TrendsFeedItemLayoutBinding
import com.yachint.twitterdrift.ui.viewholder.TrendViewHolder

class TrendAdapter(
    val context: Context
): ListAdapter<Trend, TrendViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Trend>(){
            override fun areItemsTheSame(oldItem: Trend, newItem: Trend): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Trend, newItem: Trend): Boolean {
                return oldItem.name == newItem.name && oldItem.ETA == newItem.ETA
                        && oldItem.state == newItem.state && oldItem.since == newItem.since
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendViewHolder {
        val binding = TrendsFeedItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return TrendViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: TrendViewHolder, position: Int) {
        holder.bind(position, getItem(position))
    }
}