package com.marcoscg.movies.data.sources.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteMoviesResponse(
    @SerializedName("page") val page : Int,
    @SerializedName("total_results") val total_results : Int,
    @SerializedName("total_pages") val total_pages : Int,
    @SerializedName("results") val results : List<RemoteMovie>
)