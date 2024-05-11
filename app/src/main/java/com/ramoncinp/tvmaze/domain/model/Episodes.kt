package com.ramoncinp.tvmaze.domain.model

import com.ramoncinp.tvmaze.data.model.Episode

data class Episodes(
    val error: String? = null,
    val episodes: List<Episode>
)
