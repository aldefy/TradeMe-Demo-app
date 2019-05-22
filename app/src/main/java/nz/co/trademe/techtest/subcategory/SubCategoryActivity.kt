package nz.co.trademe.techtest.subcategory

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import nz.co.trademe.techtest.home.presentation.*
import nz.co.trademe.techtest.listingsdetail.SearchListingDetailsActivity
import nz.co.trademe.techtest.subcategory.presentation.*
import nz.co.trademe.wrapper.base.BaseViewModelActivity
import nz.co.trademe.wrapper.base.ViewModelFactory
import nz.co.trademe.wrapper.base.plusAssign
import javax.inject.Inject

class SubCategoryActivity : BaseViewModelActivity<SubCategoryViewModel, SubCategoryState>() {

    companion object {
        val EXTRA_CATEGORY_ID = "category_id"
        val EXTRA_CATEGORY_NAME = "category_name"

        fun newIntent(context: Context, id: String, name: String): Intent {
            return Intent(context, SubCategoryActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY_ID, id)
                putExtra(EXTRA_CATEGORY_NAME, name)
            }
        }
    }

    override val clazz: Class<SubCategoryViewModel> by lazy { SubCategoryViewModel::class.java }
    private val view by lazy { SubCategoryViewImpl(findViewById(R.id.content)) }
    private val screen by lazy { SubCategoryScreen() }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override val vm by lazy {
        ViewModelProviders.of(this@SubCategoryActivity, viewModelFactory).get(SubCategoryViewModel::class.java)
    }
    var categoryId: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(nz.co.trademe.techtest.R.layout.activity_sub_category)
        setObservers()
        if (intent.hasExtra(EXTRA_CATEGORY_ID))
            categoryId = intent.extras.getString(EXTRA_CATEGORY_ID)
        vm.generalSearch(categoryId,0)
    }

    override fun getLayoutId() = nz.co.trademe.techtest.R.layout.activity_sub_category

    override fun getLifecycleObserver() = screen

    override fun setupView() {
        compositeBag += screen.bind(view, state.share())
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (intent.hasExtra(EXTRA_CATEGORY_NAME))
                title = intent.extras.getString(EXTRA_CATEGORY_NAME)
        }
        compositeBag += view.searchListingView.setListingsListener()
            ?.subscribe { listing ->
                startActivity(SearchListingDetailsActivity.newIntent(this@SubCategoryActivity,listing.listingId,listing.title!!))
            }!!
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
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
        fun reloadSearchListings() {
            vm.generalSearch(categoryId,0)
        }

        screen.event.observe(this, Observer { event ->
            when (event) {
                is SubCategoryEvent.ReloadSearchListing -> reloadSearchListings()
            }
        })
    }
}
