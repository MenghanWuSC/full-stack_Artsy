package com.artsy.myapplication.network

import com.squareup.moshi.Json

data class ArtsyGene (
    val name: String?,
    val description: String?,
    @Json(name = "_links") val otherLinks: OtherLinks
)