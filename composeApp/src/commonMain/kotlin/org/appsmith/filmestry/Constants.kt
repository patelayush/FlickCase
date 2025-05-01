package org.appsmith.filmestry

const val APP_NAME = "Filmestry"
const val tmdbApiHost = "https://api.themoviedb.org/3"

enum class ERROR_MSG(val msg: String) {
    EMPTY_RESPONSE("Empty Response"),
    UNKNOWN("Unknown Error"),
    API_ERROR("API Error")
}


// screens
enum class Screen(val screenName: String, val route: String) {
    Welcome("Welcome", "welcome"),
    Home("Home", "home"),
    Search("Search", "search"),
    Genres("Genres", "genres"),
}