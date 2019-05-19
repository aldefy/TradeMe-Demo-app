package nz.co.trademe.wrapper.models

import com.squareup.moshi.Json

/**
 * Listing object returned from search endpoints
 */
data class ListedItemDetail(

    /** The ID of the listing.  */
    @Json(name = "ListingId")
    val listingId: Long,

    /** The listing title.  */
    @Json(name = "Title")
    val title: String? = null,

    /** The listing category.  */
    @Json(name = "Category")
    val category: String? = null,

    /** The start price.  */
    @Json(name = "StartPrice")
    val startPrice: Double = 0.toDouble(),

    /** The Buy Now price.  */
    @Json(name = "BuyNowPrice")
    val buyNowPrice: Double = 0.toDouble(),

    /**
     * Collection containing photo urls
     */
    @Json(name = "Photos")
    val photos: List<Photo>? = null,

    /**
     * The price, in a format suitable for displaying to the user. Some categories may have special
     * pricing rules, e.g. properties may have "Price by negotiation".
     */
    @Json(name = "PriceDisplay")
    val priceDisplay: String? = null
)