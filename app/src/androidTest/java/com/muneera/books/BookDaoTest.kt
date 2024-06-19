package com.muneera.books

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.room.Room
import com.muneera.books.model.database.BookDao
import com.muneera.books.model.database.BookDatabase
import com.muneera.books.model.response.BookResponseItem
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var database: BookDatabase
    private lateinit var bookDao: BookDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java).build()
        bookDao = database.bookDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetBook() = runBlocking {
        val book = BookResponseItem(id = 1, title = "Test Book", author = "Test Author")
        bookDao.insertAll(book)

        val fetchedBook = bookDao.getBook(1)
        assertEquals(fetchedBook.title, "Test Book")
        assertEquals(fetchedBook.author, "Test Author")
    }

    @Test
    fun getAllBooks() = runBlocking {
        val book1 = BookResponseItem(id = 1, title = "Test Book 1", author = "Test Author 1")
        val book2 = BookResponseItem(id = 2, title = "Test Book 2", author = "Test Author 2")
        bookDao.insertAll(book1, book2)

        val books = bookDao.getAllBooks()
        assertEquals(books.size, 2)
        assertEquals(books[0].title, "Test Book 1")
        assertEquals(books[1].title, "Test Book 2")
    }

    @Test
    fun deleteAllBooks() = runBlocking {
        val book = BookResponseItem(id = 1, title = "Test Book", author = "Test Author")
        bookDao.insertAll(book)
        bookDao.deleteAllBooks()

        val books = bookDao.getAllBooks()
        assertTrue(books.isEmpty())
    }
}
