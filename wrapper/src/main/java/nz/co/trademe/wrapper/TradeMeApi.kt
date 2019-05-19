package nz.co.trademe.wrapper

import androidx.annotation.WorkerThread
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import nz.co.trademe.wrapper.auth.SecurityInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * A factory for a [TradeMeApiService], a Retrofit service with a few endpoints you will need for the tech test.
 *
 * Instantiate this class with a [CallAdapter.Factory] of your choice and fill in the return types in the [TradeMeApiService]
 * depending on your choice of [CallAdapter].
 *
 */
class TradeMeApi(
    private val callAdapterFactory: CallAdapter.Factory,
    private val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
) {

    /**
     * Returns a basic [TradeMeApiService], which you can use for a network call
     *
     */
    @WorkerThread
    fun get(): TradeMeApiService {
        clientBuilder.networkInterceptors().add(SecurityInterceptor(USER_AGENT))
        clientBuilder.networkInterceptors().add(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })

        return Retrofit.Builder()
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .client(clientBuilder.build())
            .baseUrl(BASE_URL)
            .build()
            .create(TradeMeApiService::class.java)
    }

    companion object {
        const val BASE_URL = "https://api.tmsandbox.co.nz/"
        private const val USER_AGENT = "TradeMeTechTest"
    }
}