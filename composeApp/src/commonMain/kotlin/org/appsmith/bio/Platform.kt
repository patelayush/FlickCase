package org.appsmith.bio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform