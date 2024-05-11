package com.ramoncinp.tvmaze.ui.schedule

import com.ramoncinp.tvmaze.data.model.Episode

sealed class ScheduleListItem {
    data class AirtimeHeader(val airtime: String) : ScheduleListItem()
    data class EpisodeUiItem(val episode: Episode) : ScheduleListItem()
}
