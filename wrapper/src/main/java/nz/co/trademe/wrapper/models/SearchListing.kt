package nz.co.trademe.wrapper.models

import com.squareup.moshi.Json

/**
 * Listing object returned from search endpoints
 */
data class SearchListing(

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
     * The URL of the primary photo for the listing (if the listing has a photo). By default you'll
     * get a thumbnail-sized photo, but you can control the size of the photo using the photo_size
     * parameter.
     */
    @Json(name = "PictureHref")
    val pictureHref: String? = null,

    /**
     * The price, in a format suitable for displaying to the user. Some categories may have special
     * pricing rules, e.g. properties may have "Price by negotiation".
     */
    @Json(name = "PriceDisplay")
    val priceDisplay: String? = null
)