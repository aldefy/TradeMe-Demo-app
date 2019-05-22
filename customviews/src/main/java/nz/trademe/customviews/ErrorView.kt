package nz.trademe.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_error.view.*
import nz.co.trademe.wrapper.models.TradeMeAppError


class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_error, this)
    }

    fun setError(error: TradeMeAppError? = null) {
        error?.let {
            textError.text = "${it.title}\n${it.desc}"
            imageError.setImageResource(it.icon)
        }
    }
}
