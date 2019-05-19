package nz.co.trademe.wrapper.di

import dagger.Component
import nz.co.trademe.wrapper.TradeMeApiService
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface WrapperComponent {
    fun tradeMeApiService(): TradeMeApiService
}
