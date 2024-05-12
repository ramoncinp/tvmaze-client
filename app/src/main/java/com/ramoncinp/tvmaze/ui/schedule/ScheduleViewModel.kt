package com.ramoncinp.tvmaze.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.tvmaze.domain.model.ScheduleState
import com.ramoncinp.tvmaze.domain.providers.DateProvider
import com.ramoncinp.tvmaze.domain.usecase.GetScheduleUseCase
import com.ramoncinp.tvmaze.domain.usecase.ScheduleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val dateProvider: DateProvider,
    private val getScheduleUseCase: GetScheduleUseCase
) : ViewModel() {

    private val _scheduleState = MutableStateFlow(ScheduleState())
    val scheduleState = _scheduleState.asStateFlow()

    private val _availableDates = MutableLiveData<List<String>>()
    val availableDates: LiveData<List<String>> = _availableDates

    private var selectedDate = dateProvider.getToday()
    private var followingDays = dateProvider.getFollowingDays()

    init {
        getFollowingDays()
        getSchedule()
    }

    private fun getFollowingDays() {
        followingDays = dateProvider.getFollowingDays()
        selectedDate = followingDays.first()
        _availableDates.value = followingDays
    }

    fun selectDate(date: String) {
        selectedDate = date
        getSchedule()
    }

    private fun getSchedule() {
        viewModelScope.launch {
            _scheduleState.update {
                it.copy(
                    isLoading = true
                )
            }
            delay(500)
            when(val scheduleResponse = getScheduleUseCase(selectedDate)) {
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
