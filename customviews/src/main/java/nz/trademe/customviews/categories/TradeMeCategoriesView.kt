package nz.trademe.customviews.categories

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_categories.view.*
import nz.co.trademe.techtest.utils.asVerticalList
import nz.co.trademe.wrapper.models.Category
import nz.trademe.customviews.R
import nz.trademe.customviews.TradeMeAdapter


class TradeMeCategoriesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val item: PublishSubject<Category> = PublishSubject.create()
    private var adapter: TradeMeAdapter<Category, TradeMeCategoriesViewHolder>? = null

    init {
        View.inflate(context, R.layout.layout_categories, this)
        setupAdapter()
        setupRecyclerView()
    }

    private fun setupAdapter() {
        adapter = TradeMeAdapter(
            { parent, _ ->
                TradeMeCategoriesViewHolder.inflate(parent).apply {
                    setClickListener(adapter, this, item)
                }
            }, { viewHolder, _, item ->
                viewHolder.bind(item)
            })
    }

    private fun setupRecyclerView() {
        listCategories
            .apply {
                adapter = this@TradeMeCategoriesView.adapter
            }.asVerticalList()
    }

    fun setCategories(model: Category?) {
        adapter?.items?.clear()
        adapter?.safeAddAll(model?.subcategories)
    }

    fun setCategoriesListener() = item.hide()
}
