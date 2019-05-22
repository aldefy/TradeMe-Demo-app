package nz.co.trademe.techtest.listingsdetail.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.co.trademe.techtest.listingsdetail.domain.SearchListingsUseCase
import nz.co.trademe.wrapper.base.BaseViewModel
import nz.co.trademe.wrapper.base.plusAssign
import javax.inject.Inject


class SearchListingsViewModel @Inject constructor(
    private val searchListingsUseCase: SearchListingsUseCase
    //  private val networkErrorHandler: TradeMeNetworkErrorHandler
) : BaseViewModel() {
    private val _state = MutableLiveData<SearchListingsState>()
    val state: LiveData<SearchListingsState> = _state

    fun getListing(listingId:Long) {
        compositeBag += searchListingsUseCase.getListing(listingId = listingId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.value = SearchListingsState.Loading.ShowLoading }
            .subscribe({
                _state.value = SearchListingsState.Loading.HideLoading
                _state.value = SearchListingsState.Content.GetLisitingSuccess(it)
            }, {
                _state.value = SearchListingsState.Loading.HideLoading
                _state.value = SearchListingsState.Error.UpdateListingError
            })
    }
}
