package com.muneera.books

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muneera.books.model.database.BookDao
import com.muneera.books.model.repository.ApiResponse
import com.muneera.books.model.repository.BookRepository
import com.muneera.books.model.response.BookResponseItem
import com.muneera.books.ui.books.BooksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.junit.*
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BooksViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: BookRepository

    @Mock
    private lateinit var bookDao: BookDao

    private lateinit var viewModel: BooksViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BooksViewModel(repository, bookDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_fetchFromRemote_success() = testScope.runTest {
        // Given
        val bookResponse = listOf(BookResponseItem(1, "Title", "Author", "Description"))
        whenever(repository.getBooks()).thenReturn(ApiResponse.Success(bookResponse))

        // When
        viewModel.fetchFromRemote()

        // Then
        advanceUntilIdle() // Ensures all coroutines are completed

        // Logging for debugging purposes
        println("loading: ${viewModel.loading.value}")
        println("error: ${viewModel.error.value}")
        println("booksState: ${viewModel.booksState.value}")

        assert(!viewModel.loading.value)  // Loading should be false after completion
        assert(viewModel.error.value == null)  // Error should be null on success
        assert(viewModel.booksState.value == bookResponse)  // Books state should be the response
        verify(bookDao).deleteAllBooks()
        verify(bookDao).insertAll(*bookResponse.toTypedArray())
    }

    @Test
    fun test_fetchFromRemote_failure() = testScope.runTest {
        // Given
        val errorMessage = "Error occurred"
        whenever(repository.getBooks()).thenReturn(ApiResponse.Error(Exception(errorMessage)))
        val bookList = listOf(BookResponseItem(1, "Cached Title", "Cached Author", "Cached Description"))
        whenever(bookDao.getAllBooks()).thenReturn(bookList)

        // When
        viewModel.fetchFromRemote()

        // Then
        advanceUntilIdle() // Ensures all coroutines are completed

        // Logging for debugging purposes
        println("loading: ${viewModel.loading.value}")
        println("error: ${viewModel.error.value}")
        println("booksState: ${viewModel.booksState.value}")

        assert(!viewModel.loading.value)
        assert(viewModel.error.value == errorMessage)
        assert(viewModel.booksState.value == bookList)
    }

    @Test
    fun test_fetchFromDb_with_empty_list() = testScope.runTest {
        // Given
        val errorMessage = "Database is empty"
        whenever(bookDao.getAllBooks()).thenReturn(emptyList())

        // When
        viewModel.fetchFromDatabase(errorMessage)

        // Then
        advanceUntilIdle() // Ensures all coroutines are completed
        assert(viewModel.booksState.value.isEmpty())
        assert(viewModel.error.value == errorMessage)
    }

    @Test
    fun test_fetchFromDb_with_non_empty_list() = testScope.runTest {
        // Given
        val bookList = listOf(BookResponseItem(1, "Cached Title", "Cached Author", "Cached Description"))
        whenever(bookDao.getAllBooks()).thenReturn(bookList)

        // When
        viewModel.fetchFromDatabase(null)

        // Then
        advanceUntilIdle() // Ensures all coroutines are completed
        assert(viewModel.booksState.value == bookList)
        assert(viewModel.error.value == null)
    }
}
