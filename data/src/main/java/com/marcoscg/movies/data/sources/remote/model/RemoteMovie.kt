package com.marcoscg.movies.data.sources.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteMovie (
    @SerializedName("popularity") val popularity : Double?,
    @SerializedName("vote_count") val vote_count : Int?,
    @SerializedName("video") val video : Boolean?,
    @SerializedName("poster_path") val poster_path : String?,
    @SerializedName("id") val id : Int,
    @SerializedName("adult") val adult : Boolean?,
    @SerializedName("backdrop_path") val backdrop_path : String?,
    @SerializedName("original_language") val original_language : String?,
    @SerializedName("original_title") val original_title : String?,
    @SerializedName("title") val title : String,
    @SerializedName("vote_average") val vote_average : Double?,
    @SerializedName("overview") val overview : String?,
    @SerializedName("release_date") val release_date : String?
)