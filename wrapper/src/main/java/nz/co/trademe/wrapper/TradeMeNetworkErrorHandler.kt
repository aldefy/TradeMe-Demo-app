package nz.co.trademe.wrapper

import com.google.gson.Gson
import nz.co.trademe.wrapper.models.TradeMeErrorResponse
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface NetworkHandler {
    fun getErrorType(
        throwable: Throwable,
        networkError: (() -> Unit)? = null,
        serverError: ((message: String?) -> Unit)? = null,
        serviceIsNotFound: (() -> Unit)? = null,
        authError: (() -> Unit)? = null,
        defaultError: ((message: String?) -> Unit)? = null
    )

    fun httpExceptionFactory(
        code: Int,
        message: String? = null,
        body: String? = null
    ): HttpException
}

class TradeMeNetworkErrorHandler : NetworkHandler {
    override fun getErrorType(
        throwable: Throwable,
        networkError: (() -> Unit)?,
        serverError: ((message: String?) -> Unit)?,
        serviceIsNotFound: (() -> Unit)?,
        authError: (() -> Unit)?,
        defaultError: ((message: String?) -> Unit)?
    ) {
        val errorResponse = getErrorResponse(throwable)

        if (isFlakyNetworkErrorOrNonHttpError(throwable)) {
            networkError?.invoke()
            return
        }

        if (isAuthFailure(throwable)) {
            authError?.invoke()
            return
        }

        if (isServiceIsNotAvailable(throwable)) {
            serviceIsNotFound?.invoke()
            return
        }

        if (isServerError(throwable)) {
            serverError?.invoke(errorResponse?.message)
            return
        }

        defaultError?.invoke(errorResponse?.message)
    }

    override fun httpExceptionFactory(code: Int, message: String?, body: String?): HttpException {
        val httpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("example.com")
            .build()
        val request = okhttp3.Request.Builder()
            .url(httpUrl)
            .build()
        val response = okhttp3.Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .header("name", "value")
            .message(message ?: "")
            .body(ResponseBody.create(MediaType.parse("application/json"), body ?: "{}"))
            .code(code)
            .build()
        val response1 = Response.error<HttpException>(
            ResponseBody.create(
                MediaType.parse("application/json"), body
                    ?: "{}"), response)
        return HttpException(response1)
    }

    private fun isFlakyNetworkErrorOrNonHttpError(error: Throwable): Boolean {
        return error is SocketException ||
                error is UnknownHostException ||
                error is SocketTimeoutException
    }

    private fun isAuthFailure(error: Throwable): Boolean {
        if (error is HttpException) {
            return error.code() == HttpURLConnection.HTTP_UNAUTHORIZED
        }
        return false
    }

    private fun isServerError(error: Throwable): Boolean {
        if (error is HttpException) {
            return (error.code() in 500..599)
        }
        return false
    }

    private fun isServiceIsNotAvailable(error: Throwable): Boolean {
        if (error is HttpException) {
            return error.code() == HttpURLConnection.HTTP_NOT_FOUND
        }
        return false
    }

    private fun getErrorResponse(error: Throwable?): TradeMeErrorResponse? {
        if (error != null) {
            val response: TradeMeErrorResponse?
            try {
                response = getResponse(error, TradeMeErrorResponse::class.java)
            } catch (ex: Exception) {
                return null
            }
            return response
        }
        return null
    }

    @Throws(IOException::class)
    private fun <T> getResponse(error: Throwable, tClass: Class<T>): T? {
        var response: Response<*>? = null
        if (error is HttpException) {
            response = error.response()
        }
        val responseBodyString = errorBody(response)
        if (!responseBodyString.isEmptyOrNullString()) {
            try {
                val obj = Gson().fromJson<T>(responseBodyString, tClass)
                if (obj != null) {
                    return obj
                }
            } catch (ex: Exception) {
                return null
            }
        }
        return null
    }

    @Throws(IOException::class)
    private fun errorBody(response: Response<*>?): String? {
        return response?.errorBody()?.string()
    }

    private fun String?.isEmptyOrNullString(): Boolean {
        return this.isNullOrEmpty() || this.equals("null", ignoreCase = true)
    }

}
