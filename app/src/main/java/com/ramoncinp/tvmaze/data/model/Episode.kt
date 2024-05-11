package com.ramoncinp.tvmaze.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episode(
    val id: Int,
    val name: String,
    val season: Int,
    val number: Int,
    val type: String,
    val airdate: String,
    val airstamp: String,
    val runtime: Int,
    val rating: Rating?,
    val image: Image?,
    val summary: String
)
