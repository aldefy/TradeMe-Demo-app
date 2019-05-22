package nz.co.trademe.techtest.home.domain

import io.reactivex.Single
import nz.co.trademe.techtest.data.TradeMeRepository
import nz.co.trademe.wrapper.models.Category
import nz.co.trademe.wrapper.models.SearchCollection
import javax.inject.Inject

interface HomeUseCase {
    fun getTopCategoryListings(page: String): Single<Category>
    fun search(categoryNumber: String, page: Int) :Single<SearchCollection?>
}

class HomeUseCaseImpl @Inject constructor(
    private val tradeMeRepository: TradeMeRepository
) : HomeUseCase {
    override fun getTopCategoryListings( page: String): Single<Category> {
        return tradeMeRepository.getCategory(page)
    }

    override fun search(categoryNumber: String, page: Int): Single<SearchCollection?> {
        return tradeMeRepository.search(categoryNumber,page)
    }
}
