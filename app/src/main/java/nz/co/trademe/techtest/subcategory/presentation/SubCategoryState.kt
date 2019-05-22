package nz.co.trademe.techtest.subcategory.presentation

import nz.co.trademe.wrapper.base.ViewState
import nz.co.trademe.wrapper.models.SearchCollection


sealed class SubCategoryState : ViewState {
    sealed class Loading :  SubCategoryState() {
        object ShowLoading : Loading()
        object HideLoading : Loading()
    }

    sealed class Content :  SubCategoryState() {
        object UpdateSearchListings : Content()

        data class GetSearchListingsSuccess(
            val model: SearchCollection?
        ) : Content()
    }

    sealed class Error :  SubCategoryState() {
        object UpdateSearchListingsFailed : Error()
        object NoInternetConnection : Error()
        object AuthorizationError : Error()

        data class ServerError(
            val title: String? = null,
            val message: String? = null
        ) : Error()
    }
}
