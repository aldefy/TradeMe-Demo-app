package nz.co.trademe.techtest

import android.app.Activity
import androidx.multidex.MultiDexApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import nz.co.trademe.techtest.di.DaggerAppComponent
import nz.co.trademe.wrapper.di.NetworkModule
import javax.inject.Inject

class TradeMeApplication : MultiDexApplication(), HasActivityInjector {
    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    private val networkModule: NetworkModule by lazy {
        NetworkModule(this@TradeMeApplication)
    }

    override fun onCreate() {
        super.onCreate()
        val component = DaggerAppComponent.builder()
            .networkModule(networkModule)
            .build()
           component.inject(this@TradeMeApplication)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector
}
