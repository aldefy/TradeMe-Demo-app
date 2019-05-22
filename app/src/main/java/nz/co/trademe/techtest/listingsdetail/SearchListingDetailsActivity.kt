package nz.co.trademe.techtest.listingsdetail

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_home.*
import nz.co.trademe.techtest.listingsdetail.presentation.*
import nz.co.trademe.techtest.subcategory.presentation.*
import nz.co.trademe.wrapper.base.BaseViewModelActivity
import nz.co.trademe.wrapper.base.ViewModelFactory
import nz.co.trademe.wrapper.base.plusAssign
import javax.inject.Inject
import android.widget.Toast



class SearchListingDetailsActivity : BaseViewModelActivity<SearchListingsViewModel, SearchListingsState>() {

    companion object {
        val EXTRA_LISTING_ID = "listing_id"
        val EXTRA_LISTING_NAME = "listing_name"

        fun newIntent(context: Context, id: Long, name: String): Intent {
            return Intent(context, SearchListingDetailsActivity::class.java).apply {
                putExtra(EXTRA_LISTING_ID, id)
                putExtra(EXTRA_LISTING_NAME, name)
            }
        }
    }

    override val clazz: Class<SearchListingsViewModel> by lazy { SearchListingsViewModel::class.java }
    private val view by lazy { SearchListingsDetailViewImpl(findViewById(R.id.content)) }
    private val screen by lazy { SearchListingsScreen() }
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override val vm by lazy {
        ViewModelProviders.of(this@SearchListingDetailsActivity, viewModelFactory).get(SearchListingsViewModel::class.java)
    }
    var listingId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(nz.co.trademe.techtest.R.layout.activity_listing_details)
        setObservers()
        if (intent.hasExtra(EXTRA_LISTING_ID))
            listingId = intent.extras.getLong(EXTRA_LISTING_ID)
        vm.getListing(listingId)
    }

    override fun getLayoutId() = nz.co.trademe.techtest.R.layout.activity_listing_details

    override fun getLifecycleObserver() = screen

    override fun setupView() {
        compositeBag += screen.bind(view, state.share())
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (intent.hasExtra(EXTRA_LISTING_NAME))
                title = intent.extras.getString(EXTRA_LISTING_NAME)
        }
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
        fun reloadListings() {
            vm.getListing(listingId)
        }

        screen.event.observe(this, Observer { event ->
            when (event) {
                is SearchListingsEvent.ReloadListing -> reloadListings()
            }
        })
    }
}
