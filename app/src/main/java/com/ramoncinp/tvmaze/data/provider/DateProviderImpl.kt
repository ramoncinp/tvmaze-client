package com.ramoncinp.tvmaze.data.provider

import com.ramoncinp.tvmaze.data.formatter.formatDate
import com.ramoncinp.tvmaze.domain.providers.DateProvider
import java.util.Date
import javax.inject.Inject

class DateProviderImpl @Inject constructor() : DateProvider {

    private val today: Date by lazy {
        Date()
    }

    override fun getToday(): String {
        return today.formatDate()
    }
}