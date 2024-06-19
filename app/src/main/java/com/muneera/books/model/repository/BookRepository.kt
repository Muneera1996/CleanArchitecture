package com.muneera.books.model.repository

import com.muneera.books.model.response.BookResponse
import com.muneera.books.model.response.BookResponseItem

interface BookRepository {
    suspend fun getBooks(): ApiResponse<BookResponse>
    fun getBook(id:Int) : BookResponseItem?
}