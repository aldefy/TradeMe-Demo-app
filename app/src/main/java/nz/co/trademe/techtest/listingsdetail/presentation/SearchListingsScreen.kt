package nz.co.trademe.techtest.listingsdetail.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.uber.autodispose.LifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import nz.co.trademe.techtest.utils.bind
import nz.co.trademe.wrapper.base.plusAssign

class SearchListingsScreen : LifecycleObserver {
    private val _event = MutableLiveData<SearchListingsEvent>()
    fun getLifecycleProvider(event: Lifecycle.Event): LifecycleScopeProvider<Lifecycle.Event> =
        this.getLifecycleProvider(event)

    val event: LiveData<SearchListingsEvent> = _event
    fun bind(
        view: SearchListingsDetailView,
        observable: Observable<SearchListingsState>
    ): Disposable {
        val compositeBag = CompositeDisposable()

        content(view, compositeBag, observable)
        error(view, compositeBag, observable)

        return compositeBag
    }


    private fun content(
        view: SearchListingsDetailView,
        compositeBag: CompositeDisposable,
        observable: Observable<SearchListingsState>
    ) {
        compositeBag += observable
            .filter { it is SearchListingsState.Content.GetLisitingSuccess }
            .map { it as SearchListingsState.Content.GetLisitingSuccess }
            .map {
                with(view) {
                    it.model?.photos?.let {
                        if (it.size > 0)
                            Glide.with(context).load(it[0].value.large).fitCenter().into(photoListingView)
                    }
                    textListingName.text = it.model?.title ?: ""
                }
            }
            .map { Unit }
            .bind(view.showDetailsView)
    }

    private fun error(
        view: SearchListingsDetailView,
        compositeBag: CompositeDisposable,
        observable: Observable<SearchListingsState>
    ) {
    }
}
