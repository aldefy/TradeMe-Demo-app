package nz.trademe.customviews.categories

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import nz.co.trademe.techtest.utils.AdapterObserver


@Suppress("UNCHECKED_CAST")
open class TradeMeAdapter<T, VH : RecyclerView.ViewHolder>(
    private val onCreateViewHolder: ((parent: ViewGroup, viewType: Int) -> VH)? = null,
    private val onBindViewHolder: ((viewHolder: VH, position: Int, item: T) -> Unit)? = null,
    private val onViewType: ((viewType: Int) -> Int)? = null,
    private val infinite: Boolean = false,
    private val onDetachedFromWindow: ((VH) -> Unit)? = null,
    private val onDetachedFromRecyclerView: ((recyclerView: RecyclerView?) -> Unit)? = null
) : RecyclerView.Adapter<VH>(), AdapterObserver<T> {

    companion object {
        private const val TYPE_HEADER = Integer.MIN_VALUE
        private const val TYPE_FOOTER = Integer.MIN_VALUE + 1
    }

    private var header: View? = null
    private var footer: View? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    operator fun invoke(block: TradeMeAdapter<T, VH>.() -> Unit) {
        block()
    }

    init {
        this.setHasStableIds(true)
    }

    val itemSize: Int
        get() = items.size

    var items = mutableListOf<T>()
        private set

    var onGetItemViewType: ((position: Int) -> Int)? = null

    override fun getItemId(position: Int): Long {
        return position.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder?.invoke(parent, viewType)!!
    }

    override fun getItemCount(): Int {
        return (if (infinite) Int.MAX_VALUE else items.size) + (if (header != null) 1 else 0) + if (footer != null) 1 else 0
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        var newPosition = getRealPosition(position)
        if (infinite) {
            newPosition = getRealPosition(position) % itemCount
        }

        if (holder is HeaderFooterViewHolder) {
            bind(holder as HeaderFooterViewHolder, newPosition)
        } else {
            val item = items[newPosition]
            onBindViewHolder?.invoke(holder, newPosition, item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var newPosition = position
        if (infinite) {
            newPosition = position % items.size
        }

        return if (onViewType != null) {
            if (isHeader(newPosition)) {
                onViewType.invoke(TYPE_HEADER)
            } else if (isFooter(newPosition)) {
                onViewType.invoke(TYPE_FOOTER)
            } else {
                onViewType.invoke(newPosition)
            }
        } else {
            if (isHeader(newPosition)) {
                TYPE_HEADER
            } else if (isFooter(newPosition)) {
                TYPE_FOOTER
            } else {
                super.getItemViewType(newPosition)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        onDetachedFromWindow?.invoke(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        onDetachedFromRecyclerView?.invoke(recyclerView)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun add(item: T, index: Int) {
        items.add(index, item)
        notifyItemInserted(index)
    }

    override fun add(item: T) {
        items.add(item)
        notifyItemInserted(items.size)
    }

    override fun addAll(collection: Collection<T>, index: Int) {
        items.addAll(index, collection)
        notifyItemRangeInserted(index, items.size)
    }

    override infix fun clearAndAddAll(collection: Collection<T>) {
        items.clear()
        addAll(collection)
    }

    override infix fun clearAndAddAll(collection: List<T>) {
        items.clear()
        addAll(collection)
    }

    override fun addAll(collection: Collection<T>) {
        items.addAll(collection)
        notifyDataSetChanged()
    }

    override fun addAll(list: List<T>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun safeAddAll(collection: Collection<T>?) {
        collection?.let {
            items.addAll(collection)
            notifyDataSetChanged()
        }
    }

    override fun safeClearAndAddAll(collection: Collection<T>?) {
        collection?.let {
            items.apply {
                clear()
                addAll(collection)
            }
            notifyDataSetChanged()
        }
    }

    override fun remove(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemSize)
    }

    override fun remove(item: T) {
        val index = items.indexOfFirst { it == item }

        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemSize)
    }

    override fun removeRange(collection: Collection<T>) {
        collection.forEachIndexed { index, item ->
            items.remove(item)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, itemSize)
        }
    }

    override fun removeRange(vararg item: T) {
        for (i in item) {
            remove(i)
        }
    }

    override fun update(index: Int, item: T) {
        items[index] = item
        notifyItemChanged(index, item)
    }

    override fun updateAll(collection: Collection<T>) {
        collection.forEachIndexed { position, item ->
            items[position] = item
        }
        notifyDataSetChanged()
    }

    override fun safeUpdateAll(collection: Collection<T>?) {
        collection?.forEachIndexed { position, item ->
            items[position] = item
        }
        notifyDataSetChanged()
    }

    override fun updateRange(collection: List<T>) {
        collection.forEachIndexed { _, cItem ->
            items.forEachIndexed { index, item ->
                if (cItem == item) {
                    update(index, item)
                }
            }
        }
    }

    override fun updateRange(vararg item: T) {
        item.forEachIndexed { index, i ->
            update(index, i)
        }
    }

    override fun clear() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun getRealPosition(position: Int): Int {
        return position - if (header != null) 1 else 0
    }

    private fun isHeader(position: Int): Boolean {
        return header != null && position == 0
    }

    private fun isFooter(position: Int): Boolean {
        return footer != null && position == getFooterPosition()
    }

    private fun getFooterPosition(): Int {
        return (if (infinite) Int.MAX_VALUE else items.size) + if (header != null) 1 else 0
    }

    private fun detachFromParent(view: View) {
        val parent = view.parent
        parent?.let {
            (parent as ViewGroup).removeView(view)
        }
    }

    private fun bind(holder: HeaderFooterViewHolder, position: Int) {
        val holderItemView = holder.itemView as ViewGroup
        val layoutParams: ViewGroup.LayoutParams
        val viewToAdd: View

        if (isHeader(position)) {
            viewToAdd = header!!
        } else if (isFooter(position)) {
            viewToAdd = footer!!
        } else {
            return
        }

        detachFromParent(viewToAdd)
        holderItemView.removeAllViews()
        holderItemView.addView(viewToAdd)

        if (layoutManager is StaggeredGridLayoutManager) {
            if (viewToAdd.layoutParams == null) {
                layoutParams = StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            } else {
                layoutParams = StaggeredGridLayoutManager.LayoutParams(
                    viewToAdd.layoutParams.width,
                    viewToAdd.layoutParams.height
                )
            }

            layoutParams.isFullSpan = true
        } else {
            if (viewToAdd.layoutParams == null) {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            } else {
                layoutParams = ViewGroup.LayoutParams(
                    viewToAdd.layoutParams.width,
                    viewToAdd.layoutParams.height
                )
            }
        }

        holder.itemView.setLayoutParams(layoutParams)
    }

    private class HeaderFooterViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}
