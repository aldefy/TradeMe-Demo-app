package nz.co.trademe.techtest.listingsdetail.presentation

import nz.co.trademe.wrapper.base.ViewState
import nz.co.trademe.wrapper.models.ListedItemDetail


sealed class SearchListingsState : ViewState {
    sealed class Loading :  SearchListingsState() {
        object ShowLoading : Loading()
        object HideLoading : Loading()
    }

    sealed class Content :  SearchListingsState() {
        object UpdateListing : Content()

        data class GetLisitingSuccess(
            val model: ListedItemDetail?
        ) : Content()
    }

    sealed class Error :  SearchListingsState() {
        object UpdateListingError : Error()
        object NoInternetConnection : Error()
        object AuthorizationError : Error()

        data class ServerError(
            val title: String? = null,
            val message: String? = null
        ) : Error()
    }
}
