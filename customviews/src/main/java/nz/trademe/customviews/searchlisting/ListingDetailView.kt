package nz.trademe.customviews.searchlisting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_listing_detail.view.*
import nz.co.trademe.wrapper.models.ListedItemDetail
import nz.trademe.customviews.R


class ListingDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_listing_detail, this)
    }

    fun setListingData(searchListing: ListedItemDetail?) {
        searchListing?.photos?.let {
            Glide.with(context).load(it[0].value.large).fitCenter().into(imageListing)
        }
        textTitle.text = searchListing?.title ?: ""
    }
}
