package nz.co.trademe.techtest.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nz.co.trademe.techtest.home.presentation.HomeViewModel
import nz.co.trademe.techtest.subcategory.presentation.SubCategoryViewModel
import nz.co.trademe.wrapper.base.ViewModelFactory
import nz.co.trademe.wrapper.di.scope.ViewModelKey

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SubCategoryViewModel::class)
    internal abstract fun bindSubCategoryViewModel(viewModel: SubCategoryViewModel): ViewModel
}
