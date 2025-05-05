package org.appsmith.flickcase

const val APP_NAME = "FlickCase"
const val tmdbApiHost = "https://api.themoviedb.org/3"

const val trendingCategory = "Trending"
const val nowPlayingCategory = "In Theatres"

//local prefs key
const val sessionCountKey = "sessionCount"
const val regionKey = "region"

enum class ERROR_MSG(val msg: String) {
    EMPTY_RESPONSE("Empty Response"),
    REACHED_END("Reached the end of results"),
    UNKNOWN("Unknown Error"),
    API_ERROR("API Error")
}


// screens
enum class Screen(val screenName: String, val route: String) {
    Welcome("Welcome", "welcome"),
    Home("Home", "home"),
    Search("Search", "search"),
    Genres("Genres", "genres"),
    About("About", "about"),
}