package nz.co.trademe.wrapper.di

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import nz.co.trademe.wrapper.BuildConfig
import nz.co.trademe.wrapper.TradeMeApi
import nz.co.trademe.wrapper.TradeMeApiService
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Provides Network API Interface ,OkHttp Cache , OkHttp , Retrofit
 */
@Module
class NetworkModule(val context: Context) {

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return OkHttpClient.Builder()
            .connectTimeout(78000, TimeUnit.MILLISECONDS)
            .readTimeout(78000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .writeTimeout(78000, TimeUnit.MILLISECONDS)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
            .addNetworkInterceptor { chain ->
                chain.proceed(
                    chain.request().let {
                        val requestBuilder = it.newBuilder()
                        requestBuilder.addHeader("Content-Type", "application/json")
                        requestBuilder.build()
                    }
                )
            }
            .addInterceptor(ChuckInterceptor(context))
            .addInterceptor(logging)
    }

    /**
     * Provides the Post service implementation.
     * @param clientBuilder the OKHttpClient Builder used to instantiate the service
     * @return the TradeMeApiService service implementation.
     */
    @Provides
    @Singleton
    internal fun provideTradeMeApi(clientBuilder: OkHttpClient.Builder): TradeMeApiService {
        return TradeMeApi(RxJava2CallAdapterFactory.create(), clientBuilder).get()
    }

}
