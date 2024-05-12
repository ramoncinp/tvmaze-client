package com.ramoncinp.tvmaze.domain.providers

interface DateProvider {

    fun getToday(): String

    fun getFollowingDays(): List<String>
}
