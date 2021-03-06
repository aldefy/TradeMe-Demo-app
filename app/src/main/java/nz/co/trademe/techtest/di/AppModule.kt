package nz.co.trademe.techtest.di

import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import nz.co.trademe.techtest.home.HomeActivity
import nz.co.trademe.techtest.listingsdetail.SearchListingDetailsActivity
import nz.co.trademe.techtest.subcategory.SubCategoryActivity
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

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributesSubCategoryActivityInjector(): SubCategoryActivity

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributesSearchListingDetailsActivityInjector(): SearchListingDetailsActivity
}
