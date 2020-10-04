package com.marcoscg.movies.model

data class MovieDetail (
    val adult : Boolean,
    val backdrop_path : String,
    //val belongs_to_collection : Belongs_to_collection?,
    val budget : Int,
    val genres : List<Genres>,
    val homepage : String,
    val id : Int,
    val imdb_id : String,
    val original_language : String,
    val original_title : String,
    val overview : String,
    val popularity : Double,
    val poster_path : String,
    val production_companies : List<ProductionCompanies>,
    //val production_countries : List<Production_countries>,
    val release_date : String,
    val revenue : Long,
    val runtime : Int,
    //val spoken_languages : List<Spoken_languages>,
    val status : String,
    val tagline : String,
    val title : String,
    val video : Boolean,
    val vote_average : Double,
    val vote_count : Int
)

data class Genres (
    val id : Int,
    val name : String
)

data class ProductionCompanies (
    val id : Int,
    val logo_path : String,
    val name : String,
    val origin_country : String
)

fun MovieDetail.toSimple(): Movie {
    return Movie(popularity, vote_count, video, poster_path, id, adult, backdrop_path,
        original_language, original_title, title, vote_average, overview, release_date)
}