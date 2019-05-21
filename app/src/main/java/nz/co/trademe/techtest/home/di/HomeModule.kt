package nz.co.trademe.techtest.home.di

import dagger.Binds
import dagger.Module
import nz.co.trademe.techtest.di.ViewModelModule
import nz.co.trademe.techtest.home.data.HomeRepository
import nz.co.trademe.techtest.home.data.HomeRepositoryImpl
import nz.co.trademe.techtest.home.domain.HomeUseCase
import nz.co.trademe.techtest.home.domain.HomeUseCaseImpl


@Module(includes = [
    ViewModelModule::class
])
abstract class HomeModule {
      @Binds
    abstract fun repository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    abstract fun usecase(impl: HomeUseCaseImpl): HomeUseCase
}

