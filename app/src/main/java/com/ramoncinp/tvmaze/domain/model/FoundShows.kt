package com.ramoncinp.tvmaze.domain.model

import com.ramoncinp.tvmaze.data.model.Show

data class FoundShows(
    val error: String? = null,
    val shows: List<Show>
)
