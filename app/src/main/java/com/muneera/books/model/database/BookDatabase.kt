package com.muneera.books.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muneera.books.model.response.BookResponseItem

@Database(entities = [BookResponseItem::class], version = 1, exportSchema = false)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}

