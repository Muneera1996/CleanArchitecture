package com.muneera.books.ui.books

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muneera.books.model.database.BookDao
import com.muneera.books.model.repository.BookRepository
import com.muneera.books.model.repository.ApiResponse
import com.muneera.books.model.response.BookResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BookRepository,
    private val bookDao: BookDao
) : ViewModel() {

    val loading = mutableStateOf(true)
    val error: MutableState<String?> = mutableStateOf(null)
    val booksState: MutableState<List<BookResponseItem>> =
        mutableStateOf(emptyList<BookResponseItem>())


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
        fetchFromRemote()
    }


    fun fetchFromRemote() {
        loading.value = true

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                when (val response = repository.getBooks()) {
                    is ApiResponse.Loading -> {
                        Log.e("Response", "Loading")
                        // Show loading indicator
                    }

                    is ApiResponse.Success -> {
                        // Show data
                        val data = response.data
                        Log.e("Response", data.toString())
                        storeBooksLocally(data)
                    }

                    is ApiResponse.Error -> {
                        // Show error message
                        val error = response.exception.message
                        Log.e("Response", error.toString())
                        fetchFromDatabase(error)
                    }
                }
            } catch (e: Exception) {
                Log.e("Exception", e.message.toString())
                fetchFromDatabase(e.message)
            } finally {
                loading.value = false
            }
        }
    }

    suspend fun fetchFromDatabase(error: String?) {
        val books = bookDao.getAllBooks()
        booksRetrieved(books,error)
    }

    @SuppressLint("SuspiciousIndentation")
    fun booksRetrieved(bookList: List<BookResponseItem>,e: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            if (bookList.isEmpty()) {
                error.value = e
                booksState.value = emptyList()
            }
            else{
                error.value = null
                booksState.value = bookList
            }
        }

    }

    suspend fun storeBooksLocally(list: List<BookResponseItem>) {
        bookDao.deleteAllBooks()
        val result = bookDao.insertAll(*list.toTypedArray())
        booksRetrieved(list,null)
    }

}