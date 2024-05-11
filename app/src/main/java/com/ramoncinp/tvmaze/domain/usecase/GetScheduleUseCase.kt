package com.ramoncinp.tvmaze.domain.usecase

import com.ramoncinp.tvmaze.domain.mappers.toScheduleList
import com.ramoncinp.tvmaze.domain.providers.DateProvider
import com.ramoncinp.tvmaze.domain.repository.TvMazeRepository
import com.ramoncinp.tvmaze.ui.schedule.ScheduleListItem
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val repository: TvMazeRepository,
    private val dateProvider: DateProvider
) {

    suspend operator fun invoke(): ScheduleResult {
        val today = dateProvider.getToday()
        val scheduleResponse = repository.getSchedule(today)

        return if (scheduleResponse.error != null) {
            ScheduleResult.Error(scheduleResponse.error)
        } else {
            ScheduleResult.Success(scheduleResponse.episodes.toScheduleList())
        }
    }
}

sealed class ScheduleResult {
    data class Success(val schedule: List<ScheduleListItem>) : ScheduleResult()
    data class Error(val error: String) : ScheduleResult()
}
