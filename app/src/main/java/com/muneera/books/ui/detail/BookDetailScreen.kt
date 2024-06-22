package com.muneera.books.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.muneera.books.model.response.BookResponseItem
import com.muneera.books.model.utils.ExtensionFunction.formatDate
import kotlin.math.min

@Composable
fun BookDetailScreen(book: BookResponseItem?) {
//
    val scrollState = rememberScrollState()

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Surface(elevation = 4.dp) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        shape = CircleShape,
                        border = BorderStroke(
                            width = 2.dp,
                            color = Color.Green
                        )
                    ) {
                        Image(
                            painter = rememberImagePainter(data = book?.image, builder = {
                                transformations(CircleCropTransformation())
                            }),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                    }
                    Text(
                        text = book?.title ?: "default name",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }

            Text(
                text = book?.releaseDate?.let { formatDate(it) } ?: "",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
            )
            Text(
                text = book?.description ?: "",
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "Author: ${book?.author ?: ""}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

enum class MealProfilePictureState(val color: Color, val profileImageSize: Dp, val borderSize: Dp) {
    Normal(Color.Magenta, 120.dp, 8.dp),
    Expanded(Color.Green, 200.dp, 24.dp)
}