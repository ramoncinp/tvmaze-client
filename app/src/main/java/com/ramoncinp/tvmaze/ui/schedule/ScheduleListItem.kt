package com.ramoncinp.tvmaze.ui.schedule

import com.ramoncinp.tvmaze.data.model.Episode

sealed class ScheduleListItem(
    val id: String
) {
    data class AirtimeHeader(val airtime: String) : ScheduleListItem(airtime)
    data class EpisodeUiItem(val episode: Episode) : ScheduleListItem(episode.id.toString())
}
