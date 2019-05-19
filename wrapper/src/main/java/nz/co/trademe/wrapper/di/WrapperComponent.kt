package nz.co.trademe.wrapper.di

import dagger.Component
import nz.co.trademe.wrapper.TradeMeApiService
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface WrapperComponent {
    @Component.Builder
    interface Builder {
        fun build(): WrapperComponent
        fun networkModule(networkModule: NetworkModule): Builder
    }

    fun tradeMeApiService(): TradeMeApiService
}
