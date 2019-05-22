package nz.co.trademe.techtest.home.presentation

import android.content.Context
import android.view.ViewGroup
import android.widget.RelativeLayout
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_home.view.*
import nz.co.trademe.techtest.R
import nz.co.trademe.techtest.utils.RxUi
import nz.co.trademe.techtest.utils.hide
import nz.co.trademe.techtest.utils.show
import nz.trademe.customviews.TradeMeShimmerView
import nz.trademe.customviews.categories.TradeMeCategoriesView

interface HomeView {
    val categoriesView: TradeMeCategoriesView

    val contentView: RelativeLayout
    val loadingView : TradeMeShimmerView

    val context: Context
    val showCategoriesView: Function<Observable<Unit>, Disposable>
    val showLoadingView : Function<Observable<Unit>, Disposable>
    val hideLoadingView : Function<Observable<Unit>, Disposable>
}

class HomeViewImpl(
    private val view: ViewGroup
) : HomeView {

    override val context: Context
        get() = view.context
    override val categoriesView = view.rvCategories as TradeMeCategoriesView
    override val loadingView = view.viewLoading as TradeMeShimmerView
    override val contentView = view.findViewById(R.id.content) as RelativeLayout

    override val showCategoriesView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { categoriesView.show() }

    override val showLoadingView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { loadingView.show() }

    override val hideLoadingView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { loadingView.hide() }

}
