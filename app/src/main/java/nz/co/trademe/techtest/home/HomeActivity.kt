package nz.co.trademe.techtest.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import nz.co.trademe.techtest.R
import nz.co.trademe.techtest.home.presentation.*
import nz.co.trademe.wrapper.base.BaseViewModelActivity
import nz.co.trademe.wrapper.base.ViewModelFactory
import nz.co.trademe.wrapper.base.plusAssign
import javax.inject.Inject

class HomeActivity : BaseViewModelActivity<HomeViewModel, HomeState>() {


    override val clazz: Class<HomeViewModel> by lazy { HomeViewModel::class.java }
    private val view by lazy { HomeViewImpl(findViewById(android.R.id.content)) }
    private val screen by lazy { HomeScreen() }
    @Inject lateinit var viewModelFactory: ViewModelFactory
    override val vm by lazy { ViewModelProviders.of(this@HomeActivity, viewModelFactory).get(HomeViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setObservers()
        vm.getTopLevelCategories("0")
    }

    override fun getLayoutId() = R.layout.activity_home

    override fun getLifecycleObserver() = screen

    override fun setupView() {
        compositeBag += screen.bind(view, state.share())
        compositeBag += view.categoriesView.setCategoriesListener()
            ?.subscribe {

            }!!
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setObservers() {
        setupVmState()
        setupScreenEvent()
    }

    private fun setupVmState() {
        vm.state.observe(this, Observer { state ->
            state?.let { _state.onNext(it) }
        })
    }

    private fun setupScreenEvent() {
        fun reloadCategoriesEvent() {
            vm.getTopLevelCategories("0")
        }

        screen.event.observe(this, Observer { event ->
            when (event) {
                is HomeEvent.ReloadCategories -> reloadCategoriesEvent()
            }
        })
    }


}
