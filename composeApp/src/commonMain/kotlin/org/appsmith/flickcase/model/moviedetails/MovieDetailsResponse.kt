package org.appsmith.flickcase.model.moviedetails

import kotlinx.serialization.Serializable
import org.appsmith.flickcase.model.genre.Genre
import org.appsmith.flickcase.model.movies.Movie

@Serializable
data class MovieDetailsResponse(
    val adult: Boolean? = null,
    val name: String? = null,
    val backdrop_path: String? = null,
    val budget: Int? = null,
    val genres: List<Genre?>? = null,
    val homepage: String? = null,
    val id: Int? = null,
    val imdb_id: String? = null,
    val origin_country: List<String?>? = null,
    val original_language: String? = null,
    val original_title: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val poster_path: String? = null,
    val production_companies: List<ProductionCompany?>? = null,
    val production_countries: List<ProductionCountry?>? = null,
    val release_date: String? = null,
    val revenue: Int? = null,
    val runtime: Int? = null,
    val spoken_languages: List<SpokenLanguage?>? = null,
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    val vote_average: Double? = null,
    val vote_count: Int? = null,
    val created_by: List<CreatedBy>? = null,
    val episode_run_time: List<Int?>? = null,
    val first_air_date: String? = null,
    val in_production: Boolean? = false,
    val languages: List<String>? = null,
    val last_air_date: String? = null,
    val last_episode_to_air: LastEpisodeToAir? = LastEpisodeToAir(),
    val networks: List<Network>? = null,
    val next_episode_to_air: Movie? = null,
    val number_of_episodes: Int? = null,
    val number_of_seasons: Int? = null,
    val original_name: String? = null,
    val seasons: List<Season>? = null,
    val type: String? = null,
)