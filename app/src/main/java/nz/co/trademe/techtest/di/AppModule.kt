package nz.co.trademe.techtest.di

import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import nz.co.trademe.techtest.home.HomeActivity
import nz.co.trademe.wrapper.di.scope.ActivityScope

@Module(
    includes = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class
    ]
)
abstract class AppModule {

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributesMainActivityInjector(): HomeActivity
}
