package nz.co.trademe.techtest.subcategory.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.co.trademe.techtest.subcategory.domain.SubCategoryUseCase
import nz.co.trademe.wrapper.base.BaseViewModel
import nz.co.trademe.wrapper.base.plusAssign
import javax.inject.Inject


class SubCategoryViewModel @Inject constructor(
    private val subCategoryUseCase: SubCategoryUseCase
    //  private val networkErrorHandler: TradeMeNetworkErrorHandler
) : BaseViewModel() {
    private val _state = MutableLiveData<SubCategoryState>()
    val state: LiveData<SubCategoryState> = _state

    fun generalSearch(categoryNumber: String, page: Int) {
        compositeBag += subCategoryUseCase.search(categoryNumber, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.value = SubCategoryState.Loading.ShowLoading }
            .subscribe({
                _state.value = SubCategoryState.Loading.HideLoading
                if (it != null) {
                    if (it.list != null) {
                        if (it.list!!.isNotEmpty()) {
                            _state.value = SubCategoryState.Content.GetSearchListingsSuccess(it)
                        } else {
                            _state.value = SubCategoryState.Error.UpdateSearchListingsFailed
                        }
                    } else {
                        _state.value = SubCategoryState.Content.GetSearchListingsSuccess(it)
                    }
                } else {
                    _state.value = SubCategoryState.Content.GetSearchListingsSuccess(it)
                }
            }, {
                _state.value = SubCategoryState.Loading.HideLoading
                _state.value = SubCategoryState.Error.UpdateSearchListingsFailed
            })
    }
}
