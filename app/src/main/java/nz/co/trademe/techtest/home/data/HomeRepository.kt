package nz.co.trademe.techtest.home.data

import io.reactivex.Single
import nz.co.trademe.wrapper.TradeMeApiService
import nz.co.trademe.wrapper.models.Category
import nz.co.trademe.wrapper.models.ListedItemDetail
import nz.co.trademe.wrapper.models.SearchCollection
import javax.inject.Inject

interface HomeRepository {
    /**
     * Retrieve the top listings [SearchCollection] for the provided [categoryNumber]
     *
     * This is a paged request, starting at page 0
     */
    fun getTopCategoryListings(categoryNumber: String, page: Int): Single<SearchCollection>

    /**
     * Retrieve general categories
     *
     * Retrieves all or part of the Trade Me category tree.
     */
    fun getCategory(number: String): Single<Category>

    /**
     * Retrieve the details of a single listing
     */
    fun getListing(listingId: Long): Single<ListedItemDetail>
}

class HomeRepositoryImpl @Inject constructor(private val service: TradeMeApiService) : HomeRepository {
    override fun getTopCategoryListings(categoryNumber: String, page: Int): Single<SearchCollection> {
        val filters = hashMapOf(
            "category" to categoryNumber,
            "page" to page.toString(),
            "rows" to "20",
            "sort_order" to "Default"
        )

        return service.generalSearch(filters)
    }

    override fun getCategory(number: String): Single<Category> {
        return service.getCategory(number)
    }

    override fun getListing(listingId: Long): Single<ListedItemDetail> {
        return service.getListing(listingId)
    }

}
