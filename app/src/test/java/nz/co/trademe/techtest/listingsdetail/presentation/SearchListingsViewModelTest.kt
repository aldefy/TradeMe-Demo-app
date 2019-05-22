package nz.co.trademe.techtest.home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.uber.autodispose.LifecycleScopeProvider
import com.uber.autodispose.TestLifecycleScopeProvider
import io.reactivex.Single
import nz.co.trademe.techtest.data.TradeMeRepository
import nz.co.trademe.techtest.listingsdetail.domain.SearchListingsUseCaseImpl
import nz.co.trademe.techtest.listingsdetail.presentation.SearchListingsDetailView
import nz.co.trademe.techtest.listingsdetail.presentation.SearchListingsScreen
import nz.co.trademe.techtest.listingsdetail.presentation.SearchListingsState
import nz.co.trademe.techtest.listingsdetail.presentation.SearchListingsViewModel
import nz.co.trademe.techtest.utils.Rx2SchedulersOverrideRule
import nz.co.trademe.techtest.utils.load
import nz.co.trademe.techtest.utils.willReturnSingle
import nz.co.trademe.wrapper.models.ListedItemDetail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SearchListingsViewModelTest {
    @Rule
    @JvmField
    val rx2SchedulersOverrideRule: Rx2SchedulersOverrideRule =
        Rx2SchedulersOverrideRule()
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var screen: SearchListingsScreen
    @Mock
    lateinit var view: SearchListingsDetailView
    @Mock
    lateinit var observer: Observer<SearchListingsState>
    @Mock
    lateinit var repository: TradeMeRepository
    lateinit var listingResponse: ListedItemDetail
    lateinit var uc: SearchListingsUseCaseImpl
    lateinit var viewModel: SearchListingsViewModel

    @Before
    fun setUp() {
        val testLifecycleProvider = TestLifecycleScopeProvider
            .createInitial(TestLifecycleScopeProvider.TestLifecycle.STARTED)
        Mockito.`when`(screen.getLifecycleProvider(Lifecycle.Event.ON_DESTROY))
            .thenReturn(testLifecycleProvider as LifecycleScopeProvider<Lifecycle.Event>)

        uc = SearchListingsUseCaseImpl(
            tradeMeRepository = repository
        )
        viewModel = SearchListingsViewModel(
            searchListingsUseCase = uc
        )
        listingResponse = load(
            modelClass = ListedItemDetail::class.java,
            rawResource = "listings_success_response.json"
        )

    }

    @Test
    fun `fetch listing detail and assert the data exists`() {

        given(
            repository.getListing(2147775516L)
        ).willReturn(Single.just(listingResponse))

        uc.getListing(2147775516L) willReturnSingle listingResponse
        viewModel.state.observeForever(observer)
        viewModel.getListing(2147775516L)

        verify(observer).onChanged(SearchListingsState.Loading.ShowLoading)
        verify(observer).onChanged(SearchListingsState.Content.GetLisitingSuccess(listingResponse))
        verify(observer).onChanged(SearchListingsState.Loading.HideLoading)
        assert(!listingResponse.title.isNullOrEmpty())
    }

}


