package nz.co.trademe.techtest.subcategory.di

import dagger.Binds
import dagger.Module
import nz.co.trademe.techtest.di.ViewModelModule
import nz.co.trademe.techtest.subcategory.domain.SubCategoryUseCase
import nz.co.trademe.techtest.subcategory.domain.SubCategoryUseCaseImpl


@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class SubCategoryModule {
    @Binds
    abstract fun usecase(impl: SubCategoryUseCaseImpl): SubCategoryUseCase
}

