package nz.co.trademe.wrapper.models

import androidx.annotation.DrawableRes

data class TradeMeAppError(
    val title: String,
    val desc :String,
    @DrawableRes val icon: Int
)
