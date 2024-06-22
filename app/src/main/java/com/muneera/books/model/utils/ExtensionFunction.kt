package com.muneera.books.model.utils

import java.text.SimpleDateFormat
import java.util.Locale

object ExtensionFunction {
    fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("E, MMM d, ''yy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

}