package com.muneera.books.model.repository

import com.muneera.books.model.response.BookResponseItem

interface BookRepository {
    suspend fun getBooks(): ApiResponse<List<BookResponseItem>>
}