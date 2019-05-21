package nz.co.trademe.techtest.home.presentation

import nz.co.trademe.wrapper.base.ViewState
import nz.co.trademe.wrapper.models.Category

sealed class HomeState : ViewState {
    sealed class Loading : HomeState() {
        object ShowLoading : Loading()
        object HideLoading : Loading()
    }

    sealed class Content : HomeState() {
        object UpdateCategories : Content()

        data class GetCategoriesSuccess(
            val model: Category
        ) : Content()
    }

    sealed class Error : HomeState() {
        object UpdateCategoriesFailed : Error()
        object NoInternetConnection : Error()
        object AuthorizationError : Error()

        data class ServerError(
            val title: String? = null,
            val message: String? = null
        ) : Error()
    }
}
