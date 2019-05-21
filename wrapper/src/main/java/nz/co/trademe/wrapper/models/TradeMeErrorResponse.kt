package nz.co.trademe.wrapper.models

import com.google.gson.annotations.SerializedName


data class TradeMeErrorResponse(
    @SerializedName("code") val code: String?,
    @SerializedName("errors") val errors: List<String>?,
    @SerializedName("message") val message: String?
)
