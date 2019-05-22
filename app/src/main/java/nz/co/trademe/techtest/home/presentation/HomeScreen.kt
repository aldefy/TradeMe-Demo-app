package nz.co.trademe.techtest.home.presentation

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

class HomeScreen : LifecycleObserver {
    private val _event = MutableLiveData<HomeEvent>()
    fun getLifecycleProvider(event: Lifecycle.Event): LifecycleScopeProvider<Lifecycle.Event> =  this.getLifecycleProvider(event)

    val event: LiveData<HomeEvent> = _event
    fun bind(
        view: HomeView,
        observable: Observable<HomeState>
    ): Disposable {
        val compositeBag = CompositeDisposable()

        loading(view, compositeBag, observable)
        content(view, compositeBag, observable)
        error(view, compositeBag, observable)

        return compositeBag
    }


    private fun loading(
        view: HomeView,
        compositeBag: CompositeDisposable,
        observable: Observable<HomeState>
    ) {
        compositeBag += observable
            .filter { it is HomeState.Loading.ShowLoading }
            .map { Unit }
            .map {
                with(view) {
                    contentView.hide()
                    /*loadingView.apply {
                        show()
                        startAnimation()
                    }*/

                }
            }
            .subscribe()

        compositeBag += observable
            .filter { it is HomeState.Loading.HideLoading }
            .map { Unit }
            .map {
               with(view){
                 /*  loadingView.apply {
                       hide()
                       stopAnimation()
                   }*/
               }
            }
            .subscribe()

    }

    private fun content(
        view: HomeView,
        compositeBag: CompositeDisposable,
        observable: Observable<HomeState>
    ) {
        compositeBag += observable
            .filter { it is HomeState.Content.GetCategoriesSuccess }
            .map { it as HomeState.Content.GetCategoriesSuccess }
            .map {
                with(view) {
                    contentView.show()
                    categoriesView.setCategories(it.model)
                }
            }
            .map { Unit }
            .bind(view.showCategoriesView)
            }

    private fun error(
        view: HomeView,
        compositeBag: CompositeDisposable,
        observable: Observable<HomeState>
    ) {
    }
}
