package nz.trademe.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_shimmer.view.*

class TradeMeShimmerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {
    init {
        LayoutInflater.from(context).inflate(R.layout.view_shimmer, this, true)
    }

    fun startAnimation() {
        shimmer.startShimmerAnimation()
    }

    fun stopAnimation() {
        shimmer.stopShimmerAnimation()
    }
}
