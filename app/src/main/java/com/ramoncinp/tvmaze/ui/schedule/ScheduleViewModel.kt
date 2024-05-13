package com.ramoncinp.tvmaze.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.tvmaze.domain.model.ChipState
import com.ramoncinp.tvmaze.domain.model.ScheduleState
import com.ramoncinp.tvmaze.domain.providers.DateProvider
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
    dateProvider: DateProvider,
    private val getScheduleUseCase: GetScheduleUseCase
) : ViewModel() {

    private val _scheduleState = MutableStateFlow(ScheduleState())
    val scheduleState = _scheduleState.asStateFlow()

    private val _availableDates = MutableLiveData<List<ChipState>>()
    val availableDates: LiveData<List<ChipState>> = _availableDates

    private var selectedDate = dateProvider.getToday()
    private val followingDays by lazy {
        dateProvider.getFollowingDays().mapIndexed { index, day ->
            val isChecked = index == 0
            ChipState(day, isChecked)
        }.toMutableList()
    }

    init {
        getFollowingDays()
        getSchedule()
    }

    private fun getFollowingDays() {
        selectedDate = followingDays.first().name
        _availableDates.value = followingDays
    }

    fun selectDate(date: String) {
        selectedDate = date
        getSchedule()
        followingDays.apply {
            forEachIndexed { index, chipState ->
                this[index] = chipState.copy(checked = date == chipState.name)
            }
        }
    }

    private fun getSchedule() {
        viewModelScope.launch {
            _scheduleState.update {
                it.copy(
                    isLoading = true
                )
            }
            when (val scheduleResponse = getScheduleUseCase(selectedDate)) {
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
