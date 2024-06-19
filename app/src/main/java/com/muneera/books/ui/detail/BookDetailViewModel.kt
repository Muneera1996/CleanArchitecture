package com.muneera.books.ui.detail

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muneera.books.model.database.BookDao
import com.muneera.books.model.response.BookResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle,
    private val bookDao: BookDao
) : ViewModel() {

    val mealDetailResponse  = mutableStateOf<BookResponseItem?>(null)

    init {
        val bookId = savedStateHandle.get<Int>("book_id")?:0
        viewModelScope.launch(Dispatchers.IO) {
            val books = bookDao.getBook(bookId)
            withContext(Dispatchers.Main){
                mealDetailResponse.value = books
            }
        }
//        mealDetailResponse.value = repository.getBook(bookId)
    }
}