package nz.co.trademe.techtest.home.di

import dagger.Binds
import dagger.Module
import nz.co.trademe.techtest.data.TradeMeRepository
import nz.co.trademe.techtest.data.TradeMeRepositoryImpl
import nz.co.trademe.techtest.di.ViewModelModule
import nz.co.trademe.techtest.home.domain.HomeUseCase
import nz.co.trademe.techtest.home.domain.HomeUseCaseImpl


@Module(includes = [
    ViewModelModule::class
])
abstract class HomeModule {
      @Binds
    abstract fun repository(impl: TradeMeRepositoryImpl): TradeMeRepository

    @Binds
    abstract fun usecase(impl: HomeUseCaseImpl): HomeUseCase
}

