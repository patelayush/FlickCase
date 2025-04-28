package org.appsmith.filmestry

import org.appsmith.filmestry.shared.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}