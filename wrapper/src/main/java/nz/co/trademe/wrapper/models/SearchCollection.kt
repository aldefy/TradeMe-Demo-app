package nz.co.trademe.wrapper.models

import com.squareup.moshi.Json

/**
 * Data class representing a search response
 */
data class SearchCollection(
    /** The index of the current page of results (starts at 1).  */
    @Json(name = "Page")
    val page: Int = 0,

    /** The number of results in the current page.  */
    @Json(name = "PageSize")
    val pageSize: Int = 0,

    /** A list of the results in the current page.  */
    @Json(name = "List")
    val list: List<SearchListing>? = null
)