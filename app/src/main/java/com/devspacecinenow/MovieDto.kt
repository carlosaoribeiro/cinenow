package com.devspacecinenow

import com.google.gson.annotations.SerializedName
import java.io.Serializable

@kotlinx.serialization.Serializable
data class MovieDto(
    val id: Int,
    @SerializedName("original_title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String,
    val overview: String,
): Serializable {
    val posterFullPath: String
        get() = "https://image.tmdb.org/t/p/w300$posterPath"
}