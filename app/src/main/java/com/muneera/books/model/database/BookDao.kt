package com.muneera.books.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.muneera.books.model.response.BookResponseItem

@Dao
interface BookDao {

    @Insert
    suspend fun insertAll(vararg books: BookResponseItem): List<Long>

    @Query("SELECT * FROM BookResponseItem")
    suspend fun getAllBooks(): List<BookResponseItem>

    @Query("SELECT * FROM BookResponseItem WHERE id = :bookId")
    suspend fun getBook(bookId: Int): BookResponseItem

    @Query("DELETE FROM BookResponseItem")
    suspend fun deleteAllBooks()
}