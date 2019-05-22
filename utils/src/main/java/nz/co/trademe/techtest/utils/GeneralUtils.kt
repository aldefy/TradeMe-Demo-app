package nz.co.trademe.techtest.utils

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.ResponseBody
import retrofit2.HttpException

fun String?.getOrEmpty(): String {
    return if (this == null || TextUtils.isEmpty(this)) "" else this
}

inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

inline var View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

inline var View.isGone: Boolean
    get() = visibility == View.GONE
    set(value) {
        visibility = if (value) View.GONE else View.VISIBLE
    }


fun View.show() {
    isVisible = true
}

fun View.hide() {
    isGone = true
}

fun View.invisible() {
    isInvisible = true
}

fun View.toggleVisibility() {
    isVisible = isVisible.not()
}

fun View.dismissKeyboardOnTouch() {
    setOnTouchListener { v, _ ->
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(v.windowToken, 0)
        false
    }
}

fun <T> Observable<T>.bind(uiFunc: Function<Observable<T>, Disposable>): Disposable = RxUi.bind(this, uiFunc)

fun RecyclerView.asVerticalList(): RecyclerView {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    itemAnimator = DefaultItemAnimator()
    return this
}

fun RecyclerView.asHorizontalList(): RecyclerView {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    itemAnimator = DefaultItemAnimator()
    return this
}

fun Any.httpExceptionFactory(
    code: Int,
    message: String? = null,
    body: String? = null
): HttpException {
    val httpUrl = HttpUrl.Builder()
        .scheme("https")
        .host("example.com")
        .build()
    val request = okhttp3.Request.Builder()
        .url(httpUrl)
        .build()
    val response = okhttp3.Response.Builder()
        .request(request)
        .protocol(Protocol.HTTP_1_1)
        .header("name", "value")
        .message(message ?: "")
        .body(ResponseBody.create(MediaType.parse("application/json"), body ?: "{}"))
        .code(code)
        .build()
    val response1 = retrofit2.Response.error<HttpException>(
        ResponseBody.create(
            MediaType.parse("application/json"), body
                ?: "{}"
        ), response
    )
    return HttpException(response1)
}

