package nz.co.trademe.techtest.di

import dagger.Component
import nz.co.trademe.techtest.TradeMeApplication
import nz.co.trademe.techtest.home.di.HomeModule
import nz.co.trademe.wrapper.di.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        (AppModule::class),
        (NetworkModule::class),
        (ViewModelModule::class),
        (HomeModule::class)
    ]
)
interface AppComponent {
    fun inject(app: TradeMeApplication)
}
