package nz.co.trademe.techtest.home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.uber.autodispose.LifecycleScopeProvider
import com.uber.autodispose.TestLifecycleScopeProvider
import io.reactivex.Single
import nz.co.trademe.techtest.home.data.HomeRepository
import nz.co.trademe.techtest.home.domain.HomeUseCase
import nz.co.trademe.techtest.home.domain.HomeUseCaseImpl
import nz.co.trademe.techtest.utils.Rx2SchedulersOverrideRule
import nz.co.trademe.techtest.utils.TrampolineSchedulerRule
import nz.co.trademe.techtest.utils.load
import nz.co.trademe.techtest.utils.willReturnSingle
import nz.co.trademe.wrapper.models.Category
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @Rule
    @JvmField
    val rx2SchedulersOverrideRule: Rx2SchedulersOverrideRule =
        Rx2SchedulersOverrideRule()
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var vm: HomeViewModel

    @Mock
    lateinit var useCase: HomeUseCase
    @Mock
    lateinit var screen: HomeScreen
    @Mock
    lateinit var view: HomeView
    @Mock
    lateinit var observer: Observer<HomeState>
    @Mock
    lateinit var repository: HomeRepository
    lateinit var categoriesResponse: Category
    lateinit var uc: HomeUseCaseImpl
    lateinit var viewModel: HomeViewModel
    val trampolineSchedulerRule = TrampolineSchedulerRule()


    @Before
    fun setUp() {
        val testLifecycleProvider = TestLifecycleScopeProvider
            .createInitial(TestLifecycleScopeProvider.TestLifecycle.STARTED)
        Mockito.`when`(screen.getLifecycleProvider(Lifecycle.Event.ON_DESTROY))
            .thenReturn(testLifecycleProvider as LifecycleScopeProvider<Lifecycle.Event>)

        uc = HomeUseCaseImpl(
            homeRepository = repository
        )
        viewModel = HomeViewModel(
            homeUseCase = useCase
        )
        categoriesResponse = load(
            modelClass = Category::class.java,
            rawResource = "categories_success_response.json"
        )
    }

    @Test
    fun `should fetch categories and toggle loading and attach categories list`() {
        given(
            useCase.getTopCategoryListings("0")
        ).willReturn(Single.just(categoriesResponse))

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
}


