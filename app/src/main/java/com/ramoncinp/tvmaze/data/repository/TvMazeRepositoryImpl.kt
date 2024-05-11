package com.ramoncinp.tvmaze.data.repository

import com.ramoncinp.tvmaze.data.service.TvMazeService
import com.ramoncinp.tvmaze.data.util.GenericRequestError
import com.ramoncinp.tvmaze.data.util.GenericServiceError
import com.ramoncinp.tvmaze.data.util.US_COUNTRY_CODE
import com.ramoncinp.tvmaze.domain.model.Episodes
import com.ramoncinp.tvmaze.domain.model.FoundShows
import com.ramoncinp.tvmaze.domain.model.Show
import javax.inject.Inject

class TvMazeRepositoryImpl @Inject constructor(
    private val service: TvMazeService
) : TvMazeRepository {

    override suspend fun getSchedule(date: String): Episodes {
        return try {
            val response = service.schedule(US_COUNTRY_CODE, date)
            val episodes = response.body().orEmpty()
            Episodes(episodes = episodes)
        } catch (e: Exception) {
            Episodes(error = GenericRequestError, episodes = listOf())
        }
    }

    override suspend fun getShow(showId: String): Show {
        return try {
            val response = service.show(showId)
            response.body()?.let {
                Show(data = it)
            } ?: Show(error = GenericServiceError, data = null)
        } catch (e: Exception) {
            Show(error = GenericRequestError, data = null)
        }
    }

    override suspend fun searchShow(query: String): FoundShows {
        return try {
            val response = service.searchShow(query)
            val shows = response.body().orEmpty().map { it.show }
            FoundShows(shows = shows)
        } catch (e: Exception) {
            FoundShows(error = GenericRequestError, shows = listOf())
        }
    }
}
