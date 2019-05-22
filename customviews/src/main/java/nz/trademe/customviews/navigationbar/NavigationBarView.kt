package nz.trademe.customviews.navigationbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_navigation.view.*
import nz.co.trademe.techtest.utils.asHorizontalList
import nz.co.trademe.wrapper.models.Category
import nz.trademe.customviews.R
import nz.trademe.customviews.TradeMeAdapter

class NavigationBarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {
    private var adapter: TradeMeAdapter<Category, TradeMeNavigationViewHolder>? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.view_navigation, this, true)
        setupAdapter()
        setupRecyclerView()
    }

    private fun setupAdapter() {
        adapter = TradeMeAdapter(
            { parent, _ ->
                TradeMeNavigationViewHolder.inflate(parent)
            }, { viewHolder, _, item ->
                viewHolder.bind(item,childCount>0)
            })
    }

    private fun setupRecyclerView() {
        listNavigation
            .apply {
                adapter = this@NavigationBarView.adapter
            }.asHorizontalList()
    }

    fun addNewCategory(model: Category) {
        adapter?.add(model)
    }
}
