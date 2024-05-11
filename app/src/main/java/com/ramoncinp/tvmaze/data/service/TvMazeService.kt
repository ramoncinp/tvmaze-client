package com.ramoncinp.tvmaze.data.service

import com.ramoncinp.tvmaze.data.model.ScheduleResponse
import com.ramoncinp.tvmaze.data.model.SearchShowResponse
import com.ramoncinp.tvmaze.data.model.Show
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val TV_MAZE_BASE_URL = "https://api.tvmaze.com/"

interface TvMazeService {

    /**
     * Fetches the schedule for the given country and date.
     *
     * @param country The country for which to fetch the schedule. E.g US, GB, etc.
     * @param date The date for which to fetch the schedule. Formatted as "YYYY-MM-DD".
     *
     * @return A [Response] object containing the [ScheduleResponse] if the request was successful,
     * or an error if the request failed.
     */
    @GET("schedule")
    suspend fun schedule(
        @Query("country") country: String,
        @Query("date") date: String
    ): Response<ScheduleResponse>

    @GET("shows/{showId}")
    suspend fun show(
        @Path("showId") showId: String
    ): Response<Show>

    @GET("search/shows")
    suspend fun searchShow(
        @Query("q") query: String
    ): Response<SearchShowResponse>
}
