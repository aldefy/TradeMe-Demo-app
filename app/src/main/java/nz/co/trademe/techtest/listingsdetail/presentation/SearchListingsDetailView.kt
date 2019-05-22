package nz.co.trademe.techtest.listingsdetail.presentation

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.activity_listing_details.view.*
import nz.co.trademe.techtest.utils.RxUi
import nz.co.trademe.techtest.utils.show


interface SearchListingsDetailView {
    val photoListingView: ImageView
    val textListingName: TextView
    val context: Context
    val showDetailsView: Function<Observable<Unit>, Disposable>
}

class SearchListingsDetailViewImpl(
    private val view: ViewGroup
) : SearchListingsDetailView {

    override val context: Context
        get() = view.context
    override val photoListingView = view.imageListing as ImageView
    override val textListingName = view.textTitle as TextView
    override val showDetailsView: Function<Observable<Unit>, Disposable> = RxUi.ui<Unit> {
        photoListingView.show()
        textListingName.show()
    }
}
