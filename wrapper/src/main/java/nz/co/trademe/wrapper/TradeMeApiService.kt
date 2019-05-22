package nz.co.trademe.wrapper

import io.reactivex.Single
import nz.co.trademe.wrapper.models.Category
import nz.co.trademe.wrapper.models.ListedItemDetail
import nz.co.trademe.wrapper.models.SearchCollection
import retrofit2.CallAdapter
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * A Retrofit service with a few endpoints you will need for the tech test.
 *
 * [TradeMeApi] is a factory to make instantiating this class easier.
 *
 * Remember to change the return types of the methods when you choose your [CallAdapter] for [TradeMeApi]
 */
interface TradeMeApiService {

    /**
     * General search
     *
     *
     * Allows you to search for listings on Trade Me by category, by keywords or a combination of
     * these two. The result set can be filtered by a variety of properties, including availability of
     * Buy Now, Pay Now, item condition or seller.Parameters listed below apply to all categories.
     * Additional parameters are available for certain categories (for example, category 3405, digital
     * cameras). Use the return_metadata parameter in combination with the category parameter to
     * retrieve information about the available parameters.
     *
     * You can use the [SearchCollection] class here.
     *
     *
     * [Trade Me Api Reference]("https://developer.trademe.co.nz/api-reference/search-methods/general-search/")
     */
    @GET("v1/Search/General.json")
    fun generalSearch(@QueryMap filters: Map<String, String>): Single<SearchCollection?>

    /**
     * Retrieve general categories
     *
     *
     * Retrieves all or part of the Trade Me category tree.
     *
     * You can use the [Category] class here.
     *
     *
     * [Trade Me Api Reference]("https://developer.trademe.co.nz/api-reference/catalogue-methods/retrieve-general-categories/")
     */
    @GET("v1/Categories/{number}.json")
    fun getCategory(@Path("number") number: String): Single<Category>

    /**
     * Retrieve the details of a single listing
     *
     * [Trade Me API Reference]("https://developer.trademe.co.nz/api-reference/listing-methods/retrieve-the-details-of-a-single-listing/")
     *
     * You can use the [ListedItemDetail] class here
     *
     */
    @GET("v1/Listings/{listingId}.json")
    fun getListing(@Path("listingId") listingId: Long): Single<ListedItemDetail>
}
