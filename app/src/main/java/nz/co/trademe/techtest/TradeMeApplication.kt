package nz.co.trademe.techtest

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import nz.co.trademe.techtest.di.DaggerAppComponent
import nz.co.trademe.wrapper.di.DaggerWrapperComponent
import nz.co.trademe.wrapper.di.NetworkModule
import nz.co.trademe.wrapper.di.WrapperComponent
import javax.inject.Inject

class TradeMeApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    private val wrapperComponent: WrapperComponent by lazy {
        DaggerWrapperComponent
            .builder()
            .networkModule(NetworkModule(applicationContext))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        val component = DaggerAppComponent.builder()
            .wrapperComponent(wrapperComponent)
            .build()
        component.inject(this@TradeMeApplication)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector
}
