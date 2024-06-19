package com.muneera.books

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.muneera.books.ui.books.BooksListingScreen
import com.muneera.books.ui.detail.BookDetailScreen
import com.muneera.books.ui.detail.BookDetailViewModel
import com.muneera.books.ui.theme.BooksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BooksTheme {
                //start defining navigation
                mealsNavigation()
            }
        }
    }
}

@Composable
fun mealsNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "book_list_screen") {
        composable(route = "book_list_screen") {
           // val categoriesViewModel: BooksViewModel = viewModel()
            BooksListingScreen{ bookId ->
                navController.navigate("book_detail_screen/$bookId")
            }
        }
        composable(
            route = "book_detail_screen/{book_id}",
            arguments = listOf(navArgument("book_id") {
                type = NavType.IntType
            })
        ) {
            val mealsDetailViewModel : BookDetailViewModel = hiltViewModel()
            BookDetailScreen(book  = mealsDetailViewModel.mealDetailResponse.value)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BooksTheme {
        mealsNavigation()
    }
}