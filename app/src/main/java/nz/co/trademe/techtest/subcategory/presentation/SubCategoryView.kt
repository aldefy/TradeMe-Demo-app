package nz.co.trademe.techtest.subcategory.presentation

import android.content.Context
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_sub_category.view.*
import nz.co.trademe.techtest.R
import nz.co.trademe.techtest.utils.RxUi
import nz.co.trademe.techtest.utils.hide
import nz.co.trademe.techtest.utils.show
import nz.trademe.customviews.ErrorView
import nz.trademe.customviews.searchlisting.SearchListingView


interface SubCategoryView {
    val searchListingView: SearchListingView

    val contentView: RelativeLayout
    val loadingView : ProgressBar
    val errorView: ErrorView


    val context: Context
    val showSearchListingView: Function<Observable<Unit>, Disposable>
    val showLoadingView : Function<Observable<Unit>, Disposable>
    val hideLoadingView : Function<Observable<Unit>, Disposable>
    val showErrorView: Function<Observable<Unit>, Disposable>
    val hideErrorView: Function<Observable<Unit>, Disposable>
}

class SubCategoryViewImpl(
    private val view: ViewGroup
) : SubCategoryView {

    override val context: Context
        get() = view.context
    override val searchListingView = view.rvSearchListing as SearchListingView
    override val loadingView = view.viewLoading as ProgressBar
    override val contentView = view.findViewById(R.id.content) as RelativeLayout
    override val errorView: ErrorView = view.viewError as ErrorView

    override val showSearchListingView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { searchListingView.show() }

    override val showLoadingView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { loadingView.show() }
    override val hideLoadingView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { loadingView.hide() }

    override val showErrorView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { errorView.show() }
    override val hideErrorView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> { errorView.hide() }

}
