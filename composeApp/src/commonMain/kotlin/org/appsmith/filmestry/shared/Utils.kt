package org.appsmith.filmestry.shared

interface Platform {
    val name: String
}


expect object AppContext

expect fun getPlatform(): Platform

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}