package nz.co.trademe.techtest.listingsdetail.presentation

sealed class SearchListingsEvent {
    object ReloadListing : SearchListingsEvent()
}
