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
import nz.co.trademe.techtest.subcategory.domain.SubCategoryUseCaseImpl
import nz.co.trademe.techtest.subcategory.presentation.SubCategoryScreen
import nz.co.trademe.techtest.subcategory.presentation.SubCategoryState
import nz.co.trademe.techtest.subcategory.presentation.SubCategoryViewImpl
import nz.co.trademe.techtest.subcategory.presentation.SubCategoryViewModel
import nz.co.trademe.techtest.utils.Rx2SchedulersOverrideRule
import nz.co.trademe.techtest.utils.load
import nz.co.trademe.techtest.utils.willReturnSingle
import nz.co.trademe.wrapper.models.SearchCollection
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SubCategoryViewModelTest {
    @Rule
    @JvmField
    val rx2SchedulersOverrideRule: Rx2SchedulersOverrideRule =
        Rx2SchedulersOverrideRule()
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var screen: SubCategoryScreen
    @Mock
    lateinit var view: SubCategoryViewImpl
    @Mock
    lateinit var observer: Observer<SubCategoryState>
    @Mock
    lateinit var repository: TradeMeRepository
    lateinit var searchCollection: SearchCollection
    lateinit var searchCollectionFailure: SearchCollection
    lateinit var uc: SubCategoryUseCaseImpl
    lateinit var viewModel: SubCategoryViewModel


    @Before
    fun setUp() {
        val testLifecycleProvider = TestLifecycleScopeProvider
            .createInitial(TestLifecycleScopeProvider.TestLifecycle.STARTED)
        Mockito.`when`(screen.getLifecycleProvider(Lifecycle.Event.ON_DESTROY))
            .thenReturn(testLifecycleProvider as LifecycleScopeProvider<Lifecycle.Event>)

        uc = SubCategoryUseCaseImpl(
            tradeMeRepository = repository
        )
        viewModel = SubCategoryViewModel(
            subCategoryUseCase = uc
        )
        searchCollection = load(
            modelClass = SearchCollection::class.java,
            rawResource = "search_collection_success_response.json"
        )
        searchCollectionFailure = load(
            modelClass = SearchCollection::class.java,
            rawResource = "search_collection_failure_response.json"
        )
    }

    @Test
    fun `should fetch search listings and toggle loading and attach Search listings list`() {

        given(
            repository.search("0010-6327-",0)
        ).willReturn(Single.just(searchCollection))

        uc.search("0010-6327-",0) willReturnSingle searchCollection
        viewModel.state.observeForever(observer)
        viewModel.generalSearch("0010-6327-",0)

        verify(observer).onChanged(SubCategoryState.Loading.ShowLoading)
        verify(observer).onChanged(SubCategoryState.Content.GetSearchListingsSuccess(searchCollection))
        verify(observer).onChanged(SubCategoryState.Loading.HideLoading)
        assert(searchCollection.list?.size == 1)
    }

    @Test
    fun `should fetch search listings and toggle loading and show error if Search listings is empty`() {

        given(
            repository.search("0341-4424-4427-",0)
        ).willReturn(Single.just(searchCollection))

        uc.search("0341-4424-4427-",0) willReturnSingle searchCollectionFailure
        viewModel.state.observeForever(observer)
        viewModel.generalSearch("0341-4424-4427-",0)

        verify(observer).onChanged(SubCategoryState.Loading.ShowLoading)
        verify(observer).onChanged(SubCategoryState.Error.UpdateSearchListingsFailed)
        verify(observer).onChanged(SubCategoryState.Loading.HideLoading)
        assert(searchCollectionFailure.list?.size == 0)
    }

}


