package nz.co.trademe.techtest.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import nz.co.trademe.techtest.home.domain.HomeUseCase
import nz.co.trademe.wrapper.base.BaseViewModel
import nz.co.trademe.wrapper.base.plusAssign
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
  //  private val networkErrorHandler: TradeMeNetworkErrorHandler
) : BaseViewModel() {
    private val _state = MutableLiveData<HomeState>()
    val state: LiveData<HomeState> = _state

    fun getTopLevelCategories(page: String) {
        compositeBag += homeUseCase.getTopCategoryListings(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _state.value = HomeState.Loading.ShowLoading }
            .subscribe({
                _state.value = HomeState.Loading.HideLoading
                _state.value = HomeState.Content.GetCategoriesSuccess(it)
            }, {
                _state.value = HomeState.Loading.HideLoading
                _state.value = HomeState.Error.UpdateCategoriesFailed
            })
    }

}
