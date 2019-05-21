package nz.co.trademe.wrapper.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing a category
 */
@JsonClass(generateAdapter = true)
data class Category(
    /**
     * A unique identifier for the category e.g. "0004-0369-6076-". We plan to change this to a
     * numeric identifier (e.g. "6076") so you should ensure you can cope with both formats.
     */
    @Json(name = "Number")
    val id: String,

    /** The name of the category. */
    @Json(name = "Name")
    val name: String,

    /** Indicates whether the category is a leaf category (i.e. has no children). */
    @Json(name = "IsLeaf")
    val isLeaf: Boolean,

    /** The list of subcategories belonging to this category.  */
    @Json(name = "Subcategories")
    var subcategories: List<Category>? = null
)
