package nz.trademe.customviews.navigationbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_navigation_bar.view.*
import nz.co.trademe.techtest.utils.hide
import nz.co.trademe.techtest.utils.show
import nz.co.trademe.wrapper.models.Category
import nz.trademe.customviews.R


class TradeMeNavigationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun inflate(parent: ViewGroup): TradeMeNavigationViewHolder {
            return TradeMeNavigationViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_navigation_bar, parent, false)
            )
        }
    }

    private val categoryTextView = view.textNavigation
    private val pathIcon = view.imagePath

    fun bind(data: Category, showPathDivider: Boolean= true) {
        categoryTextView.text = data.name
        if (showPathDivider)
            pathIcon.show()
        else
            pathIcon.hide()
    }
}
