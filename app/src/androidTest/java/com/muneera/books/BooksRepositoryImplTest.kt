package com.muneera.books

import com.muneera.books.model.api.BooksApi
import com.muneera.books.model.repository.ApiResponse
import com.muneera.books.model.repository.BooksRepositoryImpl
import com.muneera.books.model.response.BookResponseItem
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response
import org.junit.Assert.assertNotNull

class BooksRepositoryImplTest {

    private lateinit var repository: BooksRepositoryImpl
    private val api = mock(BooksApi::class.java)

    @Before
    fun setup() {
        repository = BooksRepositoryImpl(api)
    }

    @Test
    fun getBooksSuccess() = runBlocking {
        val bookList = listOf(BookResponseItem(1, "Title", "Description", "Image"))
        val response = Response.success(bookList)
        `when`(api.getBooks()).thenReturn(response)

        val result = repository.getBooks()
        assertTrue(result is ApiResponse.Success)
        assertEquals(bookList, (result as ApiResponse.Success).data)
    }

    @Test
    fun getBooksError() = runBlocking {
        `when`(api.getBooks()).thenReturn(Response.error(500, ResponseBody.create(null, "")))

        val result = repository.getBooks()
        assertTrue(result is ApiResponse.Error)
    }

}
