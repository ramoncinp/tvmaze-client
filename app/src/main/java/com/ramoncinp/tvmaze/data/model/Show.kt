package com.ramoncinp.tvmaze.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Show(
    val id: Int,
    val url: String,
    val name: String,
    val type: String,
    val language: String,
    val genres: List<String>,
    val status: String,
    val runtime: Int,
    val averageRuntime: Int,
    val premiered: String,
    val schedule: Schedule,
    val rating: Rating,
    val network: Network,
    val image: Image,
    val summary: String
)
