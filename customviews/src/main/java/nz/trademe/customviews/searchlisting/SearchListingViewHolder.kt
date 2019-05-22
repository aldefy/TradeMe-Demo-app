package nz.trademe.customviews.searchlisting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_search_listing.view.*
import nz.co.trademe.techtest.utils.formatNumberToCurrency
import nz.co.trademe.wrapper.models.SearchListing
import nz.trademe.customviews.R
import nz.trademe.customviews.TradeMeAdapter


class SearchListingViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun inflate(parent: ViewGroup): SearchListingViewHolder {
            return SearchListingViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search_listing, parent, false))
        }
    }

    private val title: TextView = view.textSearchListing
    private val startPrice: TextView = view.textStartPrice
    private val buyNowPrice: TextView = view.textBuyPrice
    private val pictureHref: ImageView = view.imageSearchListing
    private val parentLayout: CardView = view.cardParent

    fun setClickListener(
        adapter: TradeMeAdapter<SearchListing, SearchListingViewHolder>?,
        holder: SearchListingViewHolder,
        subject: PublishSubject<SearchListing>
    ) {
        itemView.setOnClickListener {
            holder.adapterPosition
                .takeIf { position -> position > 0 }
                .apply {
                    adapter?.items?.get(holder.adapterPosition)
                        ?.let { item ->
                            subject.onNext(item)
                        }
                } ?: return@setOnClickListener
        }
    }

    fun bind(data: SearchListing) {
        title.text = data.title
        startPrice.text = formatNumberToCurrency(data.startPrice)
        buyNowPrice.text = formatNumberToCurrency(data.buyNowPrice)
        Glide
            .with(itemView.context)
            .load(data.pictureHref)
            .placeholder(R.drawable.ic_alert)
            .centerCrop()
            .into(pictureHref)
        if (parentLayout.visibility == View.INVISIBLE)
            parentLayout.visibility = View.VISIBLE
    }
}
