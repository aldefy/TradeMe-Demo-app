package nz.co.trademe.techtest.listingsdetail.di

import dagger.Binds
import dagger.Module
import nz.co.trademe.techtest.di.ViewModelModule
import nz.co.trademe.techtest.listingsdetail.domain.SearchListingsUseCase
import nz.co.trademe.techtest.listingsdetail.domain.SearchListingsUseCaseImpl


@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class SearchListingsModule {
    @Binds
    abstract fun usecase(impl: SearchListingsUseCaseImpl): SearchListingsUseCase
}

