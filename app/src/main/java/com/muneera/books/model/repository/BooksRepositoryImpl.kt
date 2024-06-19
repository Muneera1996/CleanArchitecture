package com.muneera.books.model.repository

import com.muneera.books.model.api.BooksApi
import com.muneera.books.model.response.BookResponse
import com.muneera.books.model.response.BookResponseItem

class BooksRepositoryImpl(
    private val api : BooksApi
) : BookRepository {
    private var cachedMeals = listOf<BookResponseItem>()
    override suspend fun getBooks(): ApiResponse<BookResponse> {
        return try {
            ApiResponse.Loading // Emit loading state
            val response = api.getBooks()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ApiResponse.Success(data)
                 //   cachedMeals=data
                } else {
                    ApiResponse.Error(Exception("No data found"))
                }
            } else {
                ApiResponse.Error(Exception("API error with code ${response.code()}"))
            }
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }

    }

    override fun getBook(id:Int) : BookResponseItem?{
        return cachedMeals.firstOrNull { id==it.id }
    }

}