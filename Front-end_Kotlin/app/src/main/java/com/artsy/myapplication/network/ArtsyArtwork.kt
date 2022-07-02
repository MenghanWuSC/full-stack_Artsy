package com.artsy.myapplication.network

import com.squareup.moshi.Json

data class ArtsyArtwork (
    val id: String,
    val title: String,
    @Json(name = "_links") val otherLinks: OtherLinks
)