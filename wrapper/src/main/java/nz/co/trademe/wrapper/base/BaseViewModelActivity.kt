package nz.co.trademe.wrapper.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

abstract class BaseViewModelActivity<V : ViewModel, S : ViewState> : BaseActivity() {
    @Inject
    lateinit var factory: ViewModelFactory

    abstract val clazz: Class<V>

    val _state = PublishSubject.create<S>()
    val state = _state.hide()

    val compositeBag = CompositeDisposable()
    open val vm: V by lazy {
        ViewModelProviders.of(this, factory).get(clazz)
    }

    override fun onDestroy() {
        _state.onComplete()
        compositeBag.clear()
        super.onDestroy()
    }
}
