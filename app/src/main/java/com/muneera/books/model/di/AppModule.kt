package com.muneera.books.model.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.muneera.books.model.database.BookDatabase
import com.muneera.books.model.repository.BookRepository
import com.muneera.books.model.repository.BooksRepositoryImpl
import com.muneera.books.model.api.BooksApi
import com.muneera.books.model.utils.Constants.BASE_URL
import com.muneera.books.model.utils.RetryingInterceptor
import com.muneera.books.model.utils.UserDatabasePassphrase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SupportFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        supportFactory: SupportFactory
    ): BookDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = BookDatabase::class.java,
            name = "BookDatabase"
        )        .openHelperFactory(supportFactory)

            .build()
    }

    @Singleton
    @Provides
    fun provideDao(database: BookDatabase) = database.bookDao()



    @Provides
    @Singleton
    fun provideMyApi(): BooksApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(RetryingInterceptor())
            .build()

        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }


    @Provides
    @Singleton
    fun provideRepository(api : BooksApi) : BookRepository {
        return BooksRepositoryImpl(api)
    }



    @Provides
    @Singleton
    fun provideUserDatabasePassphrase(@ApplicationContext context: Context) = UserDatabasePassphrase(context)

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideSupportFactory(userDatabasePassphrase: UserDatabasePassphrase) = SupportFactory(userDatabasePassphrase.getPassphrase())

}