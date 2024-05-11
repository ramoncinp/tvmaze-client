package com.ramoncinp.tvmaze.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.tvmaze.domain.usecase.GetScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase
) : ViewModel() {

    fun getSchedule() {
        viewModelScope.launch {
            val scheduleResponse = getScheduleUseCase()
            Timber.d("Schedule: $scheduleResponse")
        }
    }
}
