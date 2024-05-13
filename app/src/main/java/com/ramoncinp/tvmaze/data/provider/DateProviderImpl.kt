package com.ramoncinp.tvmaze.data.provider

import com.ramoncinp.tvmaze.domain.providers.DateProvider
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

class DateProviderImpl @Inject constructor() : DateProvider {

    private val todayCalendar: LocalDate by lazy {
        LocalDate.now()
    }

    override fun getToday(): String {
        return todayCalendar.toString()
    }

    override fun getFollowingDays(): List<String> {
        val today = todayCalendar.toString()
        val tomorrow = todayCalendar.plusDays(1).toString()
        val afterTomorrow = todayCalendar.plusDays(2).toString()
        return listOf(
            today,
            tomorrow,
            afterTomorrow
        )
    }
}