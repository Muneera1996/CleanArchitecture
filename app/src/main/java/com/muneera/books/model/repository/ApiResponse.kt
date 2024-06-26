package com.muneera.books.model.repository

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val exception: Exception) : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()

}
