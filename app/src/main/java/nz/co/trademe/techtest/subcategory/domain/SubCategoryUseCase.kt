package nz.co.trademe.techtest.subcategory.domain

import io.reactivex.Single
import nz.co.trademe.techtest.data.TradeMeRepository
import nz.co.trademe.wrapper.models.SearchCollection
import javax.inject.Inject

interface SubCategoryUseCase {
    fun search(categoryNumber: String, page: Int) : Single<SearchCollection?>
}

class SubCategoryUseCaseImpl @Inject constructor(
    private val tradeMeRepository: TradeMeRepository
) : SubCategoryUseCase {
      override fun search(categoryNumber: String, page: Int): Single<SearchCollection?> {
        return tradeMeRepository.search(categoryNumber,page)
    }
}
