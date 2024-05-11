package com.ramoncinp.tvmaze.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.tvmaze.domain.model.ScheduleState
import com.ramoncinp.tvmaze.domain.usecase.GetScheduleUseCase
import com.ramoncinp.tvmaze.domain.usecase.ScheduleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getScheduleUseCase: GetScheduleUseCase
) : ViewModel() {

    private val _scheduleState = MutableStateFlow(ScheduleState())
    val scheduleState = _scheduleState.asStateFlow()

    fun getSchedule() {
        viewModelScope.launch {
            when(val scheduleResponse = getScheduleUseCase()) {
                is ScheduleResult.Error -> {
                    _scheduleState.update {
                        it.copy(
                            isLoading = false,
                            error = scheduleResponse.error
                        )
                    }
                }
                is ScheduleResult.Success -> {
                    _scheduleState.update {
                        it.copy(
                            isLoading = false,
                            data = scheduleResponse.schedule
                        )
                    }
                }
            }
        }
    }
}
