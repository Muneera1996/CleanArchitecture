package com.muneera.books.ui.books

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.muneera.books.model.response.BookResponseItem

@Composable
fun BooksListingScreen(navigationCallback: (Int) -> Unit) {
    val categoriesViewModel: BooksViewModel = hiltViewModel()

    val books = categoriesViewModel.booksState.value
    val error = categoriesViewModel.error.value
    val loading = categoriesViewModel.loading.value

    when {
        loading-> {
            CenteredText("Loading...")
        }
        error!=null -> {
            CenteredText("Error: $error")
        }
        books.isNotEmpty() -> {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(books) {
                    BooksCategory(book = it, navigationCallback = navigationCallback)
                }
            }
        }
    }
}

@Composable
fun CenteredText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun BooksCategory(book: BookResponseItem, navigationCallback: (Int) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clickable { navigationCallback(book.id ?: 0) }
    ) {

        Row(modifier = Modifier.animateContentSize()) {
            Image(
                painter = rememberImagePainter(book.image),
                contentDescription = null,
                modifier = Modifier
                    .size(88.dp)
                    .padding(4.dp)
                    .align(Alignment.CenterVertically)
            )
            Column(
                modifier =
                Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .fillMaxWidth(0.8f)
                    .padding(16.dp),
            ) {
                Text(text = book.title ?: "", style = MaterialTheme.typography.h6)
                CompositionLocalProvider(value = LocalContentAlpha provides ContentAlpha.high) {
                    Text(
                        text = book.description ?: "", style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (isExpanded) 10 else 4
                    )
                }
            }
            Image(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = "Keyboard Arrow Up",
                modifier = Modifier
                    .align(if (isExpanded) Alignment.Bottom else Alignment.CenterVertically)
                    .padding(16.dp)
                    .clickable {
                        isExpanded = !isExpanded
                    }
            )
        }

    }
}



