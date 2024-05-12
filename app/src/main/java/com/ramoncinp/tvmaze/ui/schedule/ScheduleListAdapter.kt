package com.ramoncinp.tvmaze.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramoncinp.tvmaze.R
import com.ramoncinp.tvmaze.data.model.Episode
import com.ramoncinp.tvmaze.databinding.AirtimeHeaderLayoutBinding
import com.ramoncinp.tvmaze.databinding.EpisodeItemLayoutBinding

class ScheduleListAdapter : ListAdapter<ScheduleListItem, RecyclerView.ViewHolder>(ScheduleListDiffCallback()) {

    companion object {
        private const val AIRTIME_HEADER_TYPE = 0
        private const val EPISODE_ELEMENT_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            AIRTIME_HEADER_TYPE -> AirtimeHeaderViewHolder.from(parent)
            EPISODE_ELEMENT_TYPE -> EpisodeItemViewHolder.from(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is AirtimeHeaderViewHolder -> holder.bind((item as ScheduleListItem.AirtimeHeader).airtime)
            is EpisodeItemViewHolder -> holder.bind((item as ScheduleListItem.EpisodeUiItem).episode)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ScheduleListItem.AirtimeHeader -> AIRTIME_HEADER_TYPE
            is ScheduleListItem.EpisodeUiItem -> EPISODE_ELEMENT_TYPE
        }
    }

    class AirtimeHeaderViewHolder(private val binding: AirtimeHeaderLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(airtime: String) {
            binding.airingTimeTv.text = binding.root.context.getString(R.string.airtime_formatted, airtime)
        }

        companion object {
            fun from(parent: ViewGroup): AirtimeHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AirtimeHeaderLayoutBinding.inflate(layoutInflater, parent, false)
                return AirtimeHeaderViewHolder(binding)
            }
        }
    }

    class EpisodeItemViewHolder(private val binding: EpisodeItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(episode: Episode) {
            binding.episodeName.text = episode.name
            binding.showName.text = episode.show.name
        }

        companion object {
            fun from(parent: ViewGroup): EpisodeItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EpisodeItemLayoutBinding.inflate(layoutInflater, parent, false)
                return EpisodeItemViewHolder(binding)
            }
        }
    }
}

class ScheduleListDiffCallback : DiffUtil.ItemCallback<ScheduleListItem>() {
    override fun areItemsTheSame(oldItem: ScheduleListItem, newItem: ScheduleListItem): Boolean {
        return when(oldItem) {
            is ScheduleListItem.AirtimeHeader -> {
                oldItem.airtime == (newItem as ScheduleListItem.AirtimeHeader).airtime
            }
            is ScheduleListItem.EpisodeUiItem -> {
                oldItem.episode.id == (newItem as ScheduleListItem.EpisodeUiItem).episode.id
            }
        }
    }

    override fun areContentsTheSame(oldItem: ScheduleListItem, newItem: ScheduleListItem): Boolean {
        return when(oldItem) {
            is ScheduleListItem.AirtimeHeader -> {
                oldItem.airtime == (newItem as ScheduleListItem.AirtimeHeader).airtime
            }
            is ScheduleListItem.EpisodeUiItem -> {
                oldItem.episode == (newItem as ScheduleListItem.EpisodeUiItem).episode
            }
        }
    }
}