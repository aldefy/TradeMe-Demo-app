package nz.co.trademe.techtest.home.presentation

import android.content.Context
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_home.view.*
import nz.co.trademe.techtest.R
import nz.co.trademe.techtest.utils.RxUi
import nz.co.trademe.techtest.utils.hide
import nz.co.trademe.techtest.utils.show
import nz.trademe.customviews.ErrorView
import nz.trademe.customviews.categories.TradeMeCategoriesView

interface HomeView {
    val categoriesView: TradeMeCategoriesView

    val contentView: RelativeLayout
    val loadingView: ProgressBar
    val errorView: ErrorView

    val context: Context
    val showCategoriesView: Function<Observable<Unit>, Disposable>
    val showLoadingView: Function<Observable<Unit>, Disposable>
    val hideLoadingView: Function<Observable<Unit>, Disposable>
    val showErrorView: Function<Observable<Unit>, Disposable>
    val hideErrorView: Function<Observable<Unit>, Disposable>
}

class HomeViewImpl(
    private val view: ViewGroup
) : HomeView {


    override val context: Context
        get() = view.context
    override val categoriesView = view.rvCategories as TradeMeCategoriesView
    override val loadingView = view.viewLoading as ProgressBar
    override val contentView = view.findViewById(R.id.content) as RelativeLayout

    override val errorView: ErrorView = view.viewError as ErrorView

    override val showCategoriesView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { categoriesView.show() }

    override val showLoadingView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { loadingView.show() }
    override val hideLoadingView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { loadingView.hide() }

    override val showErrorView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { errorView.show() }
    override val hideErrorView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { errorView.hide() }

}
