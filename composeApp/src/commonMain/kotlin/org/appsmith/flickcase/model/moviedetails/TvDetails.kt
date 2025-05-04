package org.appsmith.flickcase.model.moviedetails

import org.appsmith.flickcase.model.genre.Genre

data class TvDetails(
    val adult: Boolean? = false,
    val backdrop_path: String? = "",
    val created_by: List<CreatedBy>? = listOf(),
    val episode_run_time: List<Any?>? = listOf(),
    val first_air_date: String? = "",
    val genres: List<Genre>? = listOf(),
    val homepage: String? = "",
    val id: Int? = 0,
    val in_production: Boolean? = false,
    val languages: List<String>? = listOf(),
    val last_air_date: String? = "",
    val last_episode_to_air: LastEpisodeToAir? = LastEpisodeToAir(),
    val name: String? = "",
    val networks: List<Network>? = listOf(),
    val next_episode_to_air: Any? = Any(),
    val number_of_episodes: Int? = 0,
    val number_of_seasons: Int? = 0,
    val origin_country: List<String>? = listOf(),
    val original_language: String? = "",
    val original_name: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    val poster_path: String? = "",
    val production_companies: List<ProductionCompany>? = listOf(),
    val production_countries: List<ProductionCountry>? = listOf(),
    val seasons: List<Season>? = listOf(),
    val spoken_languages: List<SpokenLanguage>? = listOf(),
    val status: String? = "",
    val tagline: String? = "",
    val type: String? = "",
    val vote_average: Double? = 0.0,
    val vote_count: Int? = 0
)