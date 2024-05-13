package com.ramoncinp.tvmaze.domain.model

import com.ramoncinp.tvmaze.data.model.Show

data class ShowDetailState(
    val error: String? = null,
    val isLoading: Boolean = true,
    val data: Show? = null
)
