package nz.trademe.customviews.searchlisting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_search_listing.view.*
import nz.co.trademe.techtest.utils.asVerticalList
import nz.co.trademe.wrapper.models.SearchCollection
import nz.co.trademe.wrapper.models.SearchListing
import nz.trademe.customviews.R
import nz.trademe.customviews.TradeMeAdapter


class SearchListingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val item: PublishSubject<SearchListing> = PublishSubject.create()
    private var adapter: TradeMeAdapter<SearchListing, SearchListingViewHolder>? = null

    init {
        View.inflate(context, R.layout.layout_search_listing, this)
        setupAdapter()
        setupRecyclerView()
    }

    private fun setupAdapter() {
        adapter = TradeMeAdapter(
            { parent, _ ->
                SearchListingViewHolder.inflate(parent).apply {
                    setClickListener(adapter, this, item)
                }
            }, { viewHolder, _, item ->
                viewHolder.bind(item)
            })
    }

    private fun setupRecyclerView() {
        listSearchListing
            .apply {
                adapter = this@SearchListingView.adapter
            }.asVerticalList()
    }

    fun setItems(model: SearchCollection?) {
        adapter?.items?.clear()
        adapter?.safeAddAll(model?.list ?: emptyList())
    }

    fun setListingsListener() = item.hide()
}
