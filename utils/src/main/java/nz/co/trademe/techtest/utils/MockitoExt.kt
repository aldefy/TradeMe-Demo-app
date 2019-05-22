package nz.co.trademe.techtest.utils

import com.nhaarman.mockito_kotlin.given
import io.reactivex.Single
import org.mockito.BDDMockito

infix fun <T> Single<T>?.willReturnSingle(value: T): BDDMockito.BDDMyOngoingStubbing<Single<T>?> =
    given(this).willReturn(Single.just(value))

infix fun <T> Single<T>?.willReturnSingleError(value: Throwable): BDDMockito.BDDMyOngoingStubbing<Single<T>?> =
    given(this).willReturn(Single.error(value))
