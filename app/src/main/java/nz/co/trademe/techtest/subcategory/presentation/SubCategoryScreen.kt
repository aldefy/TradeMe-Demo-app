package nz.co.trademe.techtest.subcategory.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uber.autodispose.LifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import nz.co.trademe.techtest.utils.bind
import nz.co.trademe.techtest.utils.hide
import nz.co.trademe.techtest.utils.show
import nz.co.trademe.wrapper.base.plusAssign

class SubCategoryScreen : LifecycleObserver {
    private val _event = MutableLiveData<SubCategoryEvent>()
    fun getLifecycleProvider(event: Lifecycle.Event): LifecycleScopeProvider<Lifecycle.Event> =
        this.getLifecycleProvider(event)

    val event: LiveData<SubCategoryEvent> = _event
    fun bind(
        view: SubCategoryView,
        observable: Observable<SubCategoryState>
    ): Disposable {
        val compositeBag = CompositeDisposable()

        loading(view, compositeBag, observable)
        content(view, compositeBag, observable)
        error(view, compositeBag, observable)

        return compositeBag
    }


    private fun loading(
        view: SubCategoryView,
        compositeBag: CompositeDisposable,
        observable: Observable<SubCategoryState>
    ) {
        compositeBag += observable
            .filter { it is SubCategoryState.Loading.ShowLoading }
            .map { Unit }
            .map {
                with(view) {
                    with(view) {
                        loadingView.show()
                        searchListingView.hide()
                    }

                }
            }
            .subscribe()

        compositeBag += observable
            .filter { it is SubCategoryState.Loading.HideLoading }
            .map { Unit }
            .map {
                with(view) {
                    loadingView.hide()
                }
            }
            .subscribe()

    }

    private fun content(
        view: SubCategoryView,
        compositeBag: CompositeDisposable,
        observable: Observable<SubCategoryState>
    ) {
        compositeBag += observable
            .filter { it is SubCategoryState.Content.GetSearchListingsSuccess }
            .map { it as SubCategoryState.Content.GetSearchListingsSuccess }
            .map {
                with(view) {
                    searchListingView.clear()
                    searchListingView.show()
                    searchListingView.setItems(it.model)
                }
            }
            .map { Unit }
            .bind(view.showSearchListingView)
    }

    private fun error(
        view: SubCategoryView,
        compositeBag: CompositeDisposable,
        observable: Observable<SubCategoryState>
    ) {
        compositeBag += observable
            .filter { it is SubCategoryState.Error.UpdateSearchListingsFailed }
            .map { it as SubCategoryState.Error.UpdateSearchListingsFailed }
            .map {
                with(view) {
                    searchListingView.hide()
                    loadingView.hide()
                }
            }
            .map { Unit }
            .bind(view.showErrorView)
    }
}
