package nz.co.trademe.techtest.utils

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

class TrampolineSchedulerRule {
    fun start() {
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
        RxJavaPlugins.setComputationSchedulerHandler {
            Schedulers.trampoline()
        }
        RxJavaPlugins.setNewThreadSchedulerHandler {
            Schedulers.trampoline()
        }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}
