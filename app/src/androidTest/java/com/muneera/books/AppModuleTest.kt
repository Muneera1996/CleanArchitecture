package com.muneera.books

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.muneera.books.model.api.BooksApi
import com.muneera.books.model.connectivity.NetworkConnectivityObserver
import com.muneera.books.model.database.BookDao
import com.muneera.books.model.database.BookDatabase
import com.muneera.books.model.di.AppModule
import com.muneera.books.model.repository.BooksRepositoryImpl
import com.muneera.books.model.utils.UserDatabasePassphrase
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

import net.sqlcipher.database.SupportFactory
import org.mockito.Mockito.`when`

@RunWith(MockitoJUnitRunner::class)
class AppModuleTest {

    @Test
    fun testProvideDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val passphrase = "testPassphrase".toByteArray()
        val supportFactory = SupportFactory(passphrase)

        val database = AppModule.provideDatabase(context, supportFactory)
        assertNotNull(database)
        assertTrue(database is BookDatabase)
    }


    @Test
    fun testProvideDao() {
        // Mock the BookDatabase
        val database = mock(BookDatabase::class.java)

        // Create a mock for BookDao
        val mockBookDao = mock(BookDao::class.java)

        // When bookDao is called on the database mock, return the mockBookDao
        `when`(database.bookDao()).thenReturn(mockBookDao)

        // Call the method under test
        val dao = AppModule.provideDao(database)

        // Assertions
        assertNotNull(dao)
        assertTrue(dao is BookDao)
    }

    @Test
    fun testProvideMyApi() {
        val api = AppModule.provideMyApi()
        assertNotNull(api)
        assertTrue(api is BooksApi)
    }

    @Test
    fun testProvideRepository() {
        val api = mock(BooksApi::class.java)
        val repository = AppModule.provideRepository(api)
        assertNotNull(repository)
        assertTrue(repository is BooksRepositoryImpl)
    }

//    @Test
//    fun testProvideNetworkConnectivityObserver() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        val observer = AppModule.provideNetworkConnectivityObserver(context)
//        assertNotNull(observer)
//        assertTrue(observer is NetworkConnectivityObserver)
//    }

    @Test
    fun testProvideUserDatabasePassphrase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val passphrase = AppModule.provideUserDatabasePassphrase(context)
        assertNotNull(passphrase)
        assertTrue(passphrase is UserDatabasePassphrase)
    }

    @Test
    fun testProvideSupportFactory() {
        val userDatabasePassphrase = mock(UserDatabasePassphrase::class.java)
        `when`(userDatabasePassphrase.getPassphrase()).thenReturn("testPassphrase".toByteArray())
        val factory = AppModule.provideSupportFactory(userDatabasePassphrase)
        assertNotNull(factory)
        assertTrue(factory is SupportFactory)
    }
}
