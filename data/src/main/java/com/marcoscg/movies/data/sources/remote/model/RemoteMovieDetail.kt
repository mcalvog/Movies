package com.marcoscg.movies.data.sources.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteMovieDetail (
    @SerializedName("adult") val adult : Boolean?,
    @SerializedName("backdrop_path") val backdrop_path : String?,
    @SerializedName("budget") val budget : Int?,
    @SerializedName("genres") val genres : List<RemoteGenres>?,
    @SerializedName("homepage") val homepage : String?,
    @SerializedName("id") val id : Int,
    @SerializedName("imdb_id") val imdb_id : String?,
    @SerializedName("original_language") val original_language : String?,
    @SerializedName("original_title") val original_title : String?,
    @SerializedName("overview") val overview : String?,
    @SerializedName("popularity") val popularity : Double?,
    @SerializedName("poster_path") val poster_path : String?,
    @SerializedName("production_companies") val production_companies : List<RemoteProductionCompanies>?,
    @SerializedName("release_date") val release_date : String?,
    @SerializedName("revenue") val revenue : Long?,
    @SerializedName("runtime") val runtime : Int?,
    @SerializedName("status") val status : String?,
    @SerializedName("tagline") val tagline : String?,
    @SerializedName("title") val title : String,
    @SerializedName("video") val video : Boolean?,
    @SerializedName("vote_average") val vote_average : Double?,
    @SerializedName("vote_count") val vote_count : Int?
)

data class RemoteGenres (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String?
)

data class RemoteProductionCompanies (
    @SerializedName("id") val id : Int,
    @SerializedName("logo_path") val logo_path : String?,
    @SerializedName("name") val name : String?,
    @SerializedName("origin_country") val origin_country : String?
)