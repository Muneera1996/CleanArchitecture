package com.muneera.books.model.response


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class BookResponseItem(

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int?,

    @ColumnInfo(name = "author")
    @SerializedName("author")
    val author: String?,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String?=null,

    @ColumnInfo(name = "image")
    @SerializedName("image")
    val image: String?=null,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    val releaseDate: String?=null,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String?=null
)