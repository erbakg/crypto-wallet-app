package com.erbol.testnetwallet.common

/**
 * Generic sealed interface representing UI state for async operations.
 * Used across all ViewModels for consistent state handling.
 */
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

/**
 * Extension to check if state is loading
 */
val <T> UiState<T>.isLoading: Boolean
    get() = this is UiState.Loading

/**
 * Extension to get data or null
 */
fun <T> UiState<T>.getOrNull(): T? = (this as? UiState.Success)?.data

/**
 * Extension to get error message or null
 */
fun <T> UiState<T>.errorOrNull(): String? = (this as? UiState.Error)?.message
