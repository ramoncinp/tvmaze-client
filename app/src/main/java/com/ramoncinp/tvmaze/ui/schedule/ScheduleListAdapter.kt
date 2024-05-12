package com.ramoncinp.tvmaze.ui.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramoncinp.tvmaze.R
import com.ramoncinp.tvmaze.data.model.Episode
import com.ramoncinp.tvmaze.databinding.AirtimeHeaderLayoutBinding
import com.ramoncinp.tvmaze.databinding.EpisodeItemLayoutBinding
import com.squareup.picasso.Picasso

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
            with(binding) {
                episodeName.text = getEpisodeName(episode)
                showName.text = episode.show.name
                setImageToView(episode)
            }
        }

        private fun getEpisodeName(episode: Episode): String {
            val seasonAndNumber = if (episode.number != null) {
                "${episode.season}x${episode.number}"
            } else ""
            return "${episode.name} $seasonAndNumber"
        }

        private fun setImageToView(episode: Episode) {
            val showImage = binding.showImage
            if (episode.image != null) {
                Picasso.get().load(episode.image.medium).into(showImage)
                showImage.isVisible = true
            } else if (episode.show.image != null) {
                Picasso.get().load(episode.show.image.medium).into(showImage)
                showImage.isVisible = true
            } else {
                showImage.isVisible = false
            }
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
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ScheduleListItem, newItem: ScheduleListItem): Boolean {
        return oldItem == newItem
    }
}