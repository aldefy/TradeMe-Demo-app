package nz.co.trademe.wrapper.models

import com.squareup.moshi.Json

data class Value(
    @Json(name = "Thumbnail")
    val thumbnail: String?,

    @Json(name = "Large")
    val large: String?,

    @Json(name = "FullSze")
    val fullSize: String?,

    @Json(name = "PhotoId")
    val photoId: Int?
)