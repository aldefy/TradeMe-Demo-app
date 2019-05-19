package nz.co.trademe.wrapper.auth

import nz.co.trademe.wrapper.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class SecurityInterceptor(
    private val userAgent: String
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("User-Agent", this.userAgent).build()

        if (request.header("Authorization") == null) {
            val authorization =
                getHeader(BuildConfig.SANDBOX_CONSUMER_KEY, BuildConfig.SANDBOX_CONSUMER_SECRET)
            request = request.newBuilder().header("Authorization", authorization).build()
        }

        return chain.proceed(request)
    }

    companion object {

        fun getHeader(consumerKey: String, consumerSecret: String): String {
            val authorization = "OAuth oauth_consumer_key=\"%1\$s\", oauth_signature_method=\"PLAINTEXT\", oauth_signature=\"%2\$s&\""

            return String.format(authorization, consumerKey, consumerSecret)
        }
    }
}