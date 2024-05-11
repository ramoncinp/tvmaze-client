package com.ramoncinp.tvmaze.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image(
    val medium: String,
    val original: String
)
