package com.artsy.myapplication.network

import com.squareup.moshi.Json

data class HrefLink(
    val href: String
)

data class OtherLinks(
    val self: HrefLink,
    //val permalink: HrefLink,
    val thumbnail: HrefLink
)

data class ArtsyData(
    val type: String,
    val title: String,
    //val description: String?,
    //@Json(name = "og_type") val ogType: String,
    @Json(name = "_links") val otherLinks: OtherLinks
)