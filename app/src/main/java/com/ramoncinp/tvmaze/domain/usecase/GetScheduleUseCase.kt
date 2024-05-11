package com.ramoncinp.tvmaze.domain.usecase

import com.ramoncinp.tvmaze.data.model.Episode
import com.ramoncinp.tvmaze.domain.providers.DateProvider
import com.ramoncinp.tvmaze.domain.repository.TvMazeRepository
import javax.inject.Inject

class GetScheduleUseCase @Inject constructor(
    private val repository: TvMazeRepository,
    private val dateProvider: DateProvider
) {

    suspend operator fun invoke(): ScheduleResponse {
        val today = dateProvider.getToday()
        val scheduleResponse = repository.getSchedule(today)

        return if (scheduleResponse.error != null) {
            ScheduleResponse.Error(scheduleResponse.error)
        } else {
            ScheduleResponse.Success(scheduleResponse.episodes)
        }
    }
}

sealed class ScheduleResponse {
    data class Success(val schedule: List<Episode>) : ScheduleResponse()
    data class Error(val error: String) : ScheduleResponse()
}
