package com.ramoncinp.tvmaze.domain.usecase

import com.ramoncinp.tvmaze.domain.mappers.toScheduleList
import com.ramoncinp.tvmaze.domain.repository.TvMazeRepository
import com.ramoncinp.tvmaze.ui.schedule.ScheduleListItem
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val repository: TvMazeRepository
) {

    suspend operator fun invoke(date: String): ScheduleResult {
        val scheduleResponse = repository.getSchedule(date)

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
