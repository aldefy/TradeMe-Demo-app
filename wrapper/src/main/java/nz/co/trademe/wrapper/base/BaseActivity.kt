package nz.co.trademe.wrapper.base

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver

abstract class BaseActivity : AppCompatActivity() {

    private val screenObserver by lazy { getLifecycleObserver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        lifecycle.addObserver(screenObserver)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupView()
    }

    override fun onDestroy() {
        lifecycle.removeObserver(screenObserver)
        super.onDestroy()
    }

    abstract fun getLayoutId(): Int

    abstract fun getLifecycleObserver(): LifecycleObserver

    abstract fun setupView()

    fun hideSoftKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
