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
import nz.co.trademe.techtest.home.domain.HomeUseCaseImpl
import nz.co.trademe.techtest.utils.*
import nz.co.trademe.wrapper.models.Category
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection


@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @Rule
    @JvmField
    val rx2SchedulersOverrideRule: Rx2SchedulersOverrideRule =
        Rx2SchedulersOverrideRule()
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var screen: HomeScreen
    @Mock
    lateinit var view: HomeViewImpl
    @Mock
    lateinit var observer: Observer<HomeState>
    @Mock
    lateinit var repository: TradeMeRepository
    lateinit var categoriesResponse: Category
    lateinit var uc: HomeUseCaseImpl
    lateinit var viewModel: HomeViewModel


    @Before
    fun setUp() {
        val testLifecycleProvider = TestLifecycleScopeProvider
            .createInitial(TestLifecycleScopeProvider.TestLifecycle.STARTED)
        Mockito.`when`(screen.getLifecycleProvider(Lifecycle.Event.ON_DESTROY))
            .thenReturn(testLifecycleProvider as LifecycleScopeProvider<Lifecycle.Event>)

        uc = HomeUseCaseImpl(
            tradeMeRepository = repository
        )
        viewModel = HomeViewModel(
            homeUseCase = uc
        )
        categoriesResponse = load(
            modelClass = Category::class.java,
            rawResource = "categories_success_response.json"
        )
    }

    @Test
    fun `should fetch categories and toggle loading and attach categories list`() {

        given(
            repository.getCategory("0")
        ).willReturn(Single.just(categoriesResponse))

        uc.getTopCategoryListings("0") willReturnSingle categoriesResponse
        viewModel.state.observeForever(observer)
        viewModel.getTopLevelCategories("0")

        verify(observer).onChanged(HomeState.Loading.ShowLoading)
        verify(observer).onChanged(HomeState.Content.GetCategoriesSuccess(categoriesResponse))
        verify(observer).onChanged(HomeState.Loading.HideLoading)
        assert(categoriesResponse.subcategories?.size == 27)
    }

    @Test
    fun `should fetch categories and assert failed response shows error view`() {
        repository.getCategory("0") willReturnSingleError httpExceptionFactory(HttpURLConnection.HTTP_UNAVAILABLE)
        uc.getTopCategoryListings("0") willReturnSingleError httpExceptionFactory(HttpURLConnection.HTTP_UNAVAILABLE)

        viewModel.state.observeForever(observer)
        viewModel.getTopLevelCategories("0")

        verify(observer).onChanged(HomeState.Loading.ShowLoading)
        verify(observer).onChanged(HomeState.Loading.HideLoading)
        verify(observer).onChanged(HomeState.Error.UpdateCategoriesFailed)
    }
}


