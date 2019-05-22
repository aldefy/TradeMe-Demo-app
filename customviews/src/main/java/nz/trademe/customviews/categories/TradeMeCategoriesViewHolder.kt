package nz.trademe.customviews.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_categories.view.*
import nz.co.trademe.wrapper.models.Category
import nz.trademe.customviews.R
import nz.trademe.customviews.TradeMeAdapter


class TradeMeCategoriesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun inflate(parent: ViewGroup): TradeMeCategoriesViewHolder {
            return TradeMeCategoriesViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_categories, parent, false))
        }
    }

    private val category = view.tvCategoryName
    private val layout = view.layoutCategory

    fun setClickListener(
        adapter: TradeMeAdapter<Category, TradeMeCategoriesViewHolder>?,
        holder: TradeMeCategoriesViewHolder,
        subject: PublishSubject<Category>
    ) {
        layout.setOnClickListener {
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

    fun bind(data: Category) {
        category.text = data.name
    }
}
