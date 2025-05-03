package org.appsmith.flickcase.model.configuration
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationResponse(
    val change_keys: List<String?>? = null,
    val images: Images? = null
)