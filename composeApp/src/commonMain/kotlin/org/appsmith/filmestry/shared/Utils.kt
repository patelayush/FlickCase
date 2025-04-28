package org.appsmith.filmestry.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform