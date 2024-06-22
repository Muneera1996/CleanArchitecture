package com.muneera.books.model.api

import com.muneera.books.model.response.BookResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface BooksApi {
    @GET("1")
    suspend fun getBooks(): Response<List<BookResponseItem>>
}