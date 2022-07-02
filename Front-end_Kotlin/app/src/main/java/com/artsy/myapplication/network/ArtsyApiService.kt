package com.artsy.myapplication.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://artsy-default-menghanwu.wl.r.appspot.com"

/**
 * Build the Moshi object with Kotlin adapter factory that Retrofit will be using.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * The Retrofit object with the Moshi converter.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the Artsy query methods.
 */
interface ArtsyApiService {
    /**
     * Returns a [List] of Artsy data classes and this method can be called from a Coroutine.
     * @GET annotation with the HTTP method
     */

    @GET("api/search")
    suspend fun getArtists(
        @Query("query") artist: String
    ): List<ArtsyData>

    @GET("api/detail")
    suspend fun getDetails(
        @Query("id") id: String
    ): ArtsyDetail

    @GET("api/artwork")
    suspend fun getArtworks(
        @Query("id") id: String
    ): List<ArtsyArtwork>

    @GET("api/gene")
    suspend fun getGenes(
        @Query("id") id: String
    ): List<ArtsyGene>

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ArtsyApi {
    val retrofitService: ArtsyApiService by lazy {
        retrofit.create(ArtsyApiService::class.java)
    }
}