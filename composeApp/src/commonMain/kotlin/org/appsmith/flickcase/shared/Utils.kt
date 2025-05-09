package org.appsmith.flickcase.shared

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger

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

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context)
        .crossfade(true)
        .logger(DebugLogger())
        .memoryCachePolicy(CachePolicy.ENABLED)
        .memoryCache {
            MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
        }
        .build()