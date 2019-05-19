package nz.co.trademe.wrapper.models

import com.squareup.moshi.Json

data class Photo(
    @Json(name = "Key")
    val key: String,

    @Json(name = "Value")
    val value: Value
)