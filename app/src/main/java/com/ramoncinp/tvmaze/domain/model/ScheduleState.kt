package com.ramoncinp.tvmaze.domain.model

import com.ramoncinp.tvmaze.ui.schedule.ScheduleListItem

data class ScheduleState(
    val error: String? = null,
    val isLoading: Boolean = true,
    val data: List<ScheduleListItem> = emptyList()
)
