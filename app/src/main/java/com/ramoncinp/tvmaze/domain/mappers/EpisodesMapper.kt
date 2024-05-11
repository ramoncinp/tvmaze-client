package com.ramoncinp.tvmaze.domain.mappers

import com.ramoncinp.tvmaze.data.model.Episode
import com.ramoncinp.tvmaze.ui.schedule.ScheduleListItem

fun List<Episode>.toScheduleList(): List<ScheduleListItem> {
    val map = groupByAiringTime()
    val elements = mutableListOf<ScheduleListItem>()
    map.forEach { (airingTime, episodes) ->
        elements.add(ScheduleListItem.AirtimeHeader(airingTime))
        val episodesUiItems = episodes.map { ScheduleListItem.EpisodeUiItem(it) }
        elements.addAll(episodesUiItems)
    }
    return elements
}

private fun List<Episode>.groupByAiringTime(): Map<String, List<Episode>> {
    val map = mutableMapOf<String, MutableList<Episode>>()
    forEach { episode ->
        val airingTime = episode.airtime
        if (map.containsKey(airingTime)) {
            map[airingTime]?.add(episode)
        } else {
            map[airingTime] = mutableListOf(episode)
        }
    }
    return map
}
