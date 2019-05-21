package nz.co.trademe.techtest.home.domain

import io.reactivex.Single
import nz.co.trademe.techtest.home.data.HomeRepository
import nz.co.trademe.wrapper.models.Category
import javax.inject.Inject

interface HomeUseCase {
    fun getTopCategoryListings(page: String): Single<Category>
}

class HomeUseCaseImpl @Inject constructor(
    private val homeRepository: HomeRepository
) : HomeUseCase {
    override fun getTopCategoryListings( page: String): Single<Category> {
        return homeRepository.getCategory(page)
    }
}
