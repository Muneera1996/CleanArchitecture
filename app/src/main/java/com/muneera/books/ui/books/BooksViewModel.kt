package com.muneera.books.ui.books

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muneera.books.model.database.BookDao
import com.muneera.books.model.repository.BookRepository
import com.muneera.books.model.connectivity.ConnectivityObserver
import com.muneera.books.model.connectivity.NetworkConnectivityObserver
import com.muneera.books.model.repository.ApiResponse
import com.muneera.books.model.response.BookResponse
import com.muneera.books.model.response.BookResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BooksViewModel @Inject constructor(
    private val repository: BookRepository,
    private val bookDao: BookDao,
    private val connectivity: NetworkConnectivityObserver
    ) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val empty : MutableState<Boolean> = mutableStateOf(false)
    val booksState: MutableState<List<BookResponseItem>> = mutableStateOf(emptyList<BookResponseItem>())
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    private val _dataState = MutableLiveData<ApiResponse<BookResponse>>()


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    init {
        viewModelScope.launch {
            connectivity.observe().collect { network = it }
        }

        // Toast.makeText(application, "Unable to get data, server error", Toast.LENGTH_LONG).show()
//            if (network == ConnectivityObserver.Status.Available) {
//                fetchFromRemote()
//            }
//            else{
//                fetchFromRemote()
//            }

        fetchFromRemote()

    }


    private fun fetchFromRemote() {
        try {
            viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
                when (val response = repository.getBooks()) {
                    is ApiResponse.Loading -> {
                        Log.e("Response","Loading")
                        // Show loading indicator
                    }
                    is ApiResponse.Success -> {
                        // Show data
                        val data = response.data
                        Log.e("Response",data.toString())
                        storeBooksLocally(data)
                    }
                    is ApiResponse.Error -> {
                        // Show error message
                        val error = response.exception.message
                        Log.e("Response",error.toString())
                        fetchFromDatabase()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("Exception", e.message.toString())

        }
    }


    private suspend fun fetchFromDatabase() {
    //    loading.value = true
            val books = bookDao.getAllBooks()
            booksRetrieved(books)
            //Toast.makeText(application, "Books retrieved from database", Toast.LENGTH_SHORT).show()

    }

    private fun booksRetrieved(bookList: List<BookResponseItem>) {
        // dogsLoadError.value = false
        viewModelScope.launch(Dispatchers.Main) {
            booksState.value = bookList
            loading.value = false
            if (bookList.isEmpty())
            empty.value = true
        }

    }

    private fun storeBooksLocally(list: List<BookResponseItem>) {
        viewModelScope.launch {
            bookDao.deleteAllBooks()
            val result = bookDao.insertAll(*list.toTypedArray())
            booksRetrieved(list)
        }
    }

//    private suspend fun getBooks(): List<BookResponseItem> {
//        return repository.getBooks()
//    }

}