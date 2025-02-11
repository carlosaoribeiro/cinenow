package com.devspacecinenow

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspacecinenow.ui.theme.CineNowTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MovieDetailScreen(
    navHostController: NavHostController,
    movieId: String
) {
    var movieDto by remember { mutableStateOf<MovieDto?>(null) }

    val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)
    val callGetMovieById = apiService.getMovieById(movieId)

    callGetMovieById.enqueue(object : Callback<MovieDto> {
        override fun onResponse(
            call: Call<MovieDto>,
            response: Response<MovieDto>
        ) {
            if (response.isSuccessful) {
                val movieDetail = response.body()
                if (movieDetail != null) {
                    movieDto = movieDetail
                }

            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieDto>, t: Throwable) {
            Log.e("MainActivity", "Network Error :: ${t.message}")
        }
    })

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navHostController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigation icon"
                )
            }
            Text(
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                text = movieDto?.title ?: ""
            )
        }
        movieDto?.let {
            MovieDetailContent(it)
        }
    }
}

@Composable
private fun MovieDetailContent(
    movieDto: MovieDto,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            model = movieDto.posterFullPath,
            contentDescription = "${movieDto.title} Poster image"
        )
        Text(
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp,
            text = movieDto.overview
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailPreview() {
    CineNowTheme {
        val movie = MovieDto(
            id = 0,
            title = "Movie title",
            posterPath = "/gKkl37BQuKTangyYQG1pyYgLVgf.jpg",
            overview = "Movie long overview Movie long overview Movie " +
                    "long overview Movie long overview Movie " +
                    "long overview Movie long overviewMovie long " +
                    "overviewMovie long overviewMovie long overviewMovie " +
                    "long overviewMovie long overviewMovie long " +
                    "overviewMovie long overviewMovie long overviewMovie " +
                    "long overviewMovie long overview"
        )

        MovieDetailContent(movieDto = movie)
    }
}