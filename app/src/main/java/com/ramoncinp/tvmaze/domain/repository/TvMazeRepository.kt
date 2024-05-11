package com.ramoncinp.tvmaze.domain.repository

import com.ramoncinp.tvmaze.domain.model.Episodes
import com.ramoncinp.tvmaze.domain.model.FoundShows
import com.ramoncinp.tvmaze.domain.model.Show

interface TvMazeRepository {

    suspend fun getSchedule(date: String): Episodes

    suspend fun getShow(showId: String): Show

    suspend fun searchShow(query: String): FoundShows
}
