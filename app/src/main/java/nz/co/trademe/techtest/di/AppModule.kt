package nz.co.trademe.techtest.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import nz.co.trademe.techtest.TradeMeApplication
import nz.co.trademe.techtest.home.HomeActivity
import nz.co.trademe.wrapper.di.scope.ActivityScope

@Module(
    includes = [
        AndroidSupportInjectionModule::class
    ]
)
abstract class AppModule {

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributesMainActivityInjector(): HomeActivity
}
