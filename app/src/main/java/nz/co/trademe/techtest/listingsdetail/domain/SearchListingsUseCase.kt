package nz.co.trademe.techtest.listingsdetail.domain

import io.reactivex.Single
import nz.co.trademe.techtest.data.TradeMeRepository
import nz.co.trademe.wrapper.models.ListedItemDetail
import javax.inject.Inject


interface SearchListingsUseCase {
    fun getListing(listingId: Long) : Single<ListedItemDetail>
}

class SearchListingsUseCaseImpl @Inject constructor(
    private val tradeMeRepository: TradeMeRepository
) : SearchListingsUseCase {
    override fun getListing(listingId: Long): Single<ListedItemDetail> {
        return tradeMeRepository.getListing(listingId)
    }
}
