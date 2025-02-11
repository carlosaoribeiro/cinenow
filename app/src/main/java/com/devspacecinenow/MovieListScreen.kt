
package com.devspacecinenow

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun MovieListScreen(navHostController: NavHostController) {

    var nowPlayingMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }
    var popularMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }
    var upcomingMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }
    var topRatedMovies by remember { mutableStateOf<List<MovieDto>>(emptyList()) }

    val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)
    val callNowPlaying = apiService.getNowPlayingMovies()

    callNowPlaying.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                nowPlayingMovies = movies
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.e("MainActivity", "Network Error :: ${t.message}")
        }
    })

    val callPopular = apiService.getPopularMovies()
    callPopular.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                popularMovies = movies
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.e("MainActivity", "Network Error :: ${t.message}")
        }
    })

    val callUpcoming = apiService.getUpcomingMovies()
    callUpcoming.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                upcomingMovies = movies
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.e("MainActivity", "Network Error :: ${t.message}")
        }
    })

    val callTopRated = apiService.getTopRatedMovies()
    callTopRated.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                topRatedMovies = movies
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.e("MainActivity", "Network Error :: ${t.message}")
        }
    })

    MovieListContent(
        topRated = topRatedMovies,
        nowPlaying = nowPlayingMovies,
        popular = popularMovies,
        upcoming = upcomingMovies
    ) { movieClicked ->
        navHostController.navigate(route = "movieDetail/${movieClicked.id}")
    }
}

@Composable
private fun MovieListContent(
    topRated: List<MovieDto>,
    nowPlaying: List<MovieDto>,
    popular: List<MovieDto>,
    upcoming: List<MovieDto>,
    onItemClicked: (MovieDto) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "CineNow",
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.size(8.dp))
        MovieSession(
            label = "Top Rated",
            movies = topRated,
            onClick = onItemClicked
        )
        MovieSession(
            label = "Now Playing",
            movies = nowPlaying,
            onClick = onItemClicked
        )
        MovieSession(
            label = "Popular",
            movies = popular,
            onClick = onItemClicked

        )
        MovieSession(
            label = "Upcoming",
            movies = upcoming,
            onClick = onItemClicked
        )
    }
}


@Composable
private fun MovieSession(
    label: String,
    movies: List<MovieDto>,
    onClick: (MovieDto) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.size(8.dp))
        MovieList(movies = movies, onClick = onClick)
    }
}

@Composable
private fun MovieList(
    movies: List<MovieDto>,
    onClick: (MovieDto) -> Unit
) {
    LazyRow {
        items(movies) { movie ->
            MovieItem(movie, onClick)
        }
    }
}

@Composable
private fun MovieItem(
    movie: MovieDto,
    onClick: (MovieDto) -> Unit
) {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .clickable {
                onClick.invoke(movie)
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(end = 8.dp)
                .width(120.dp)
                .height(150.dp),
            contentScale = ContentScale.Crop,
            model = movie.posterFullPath,
            contentDescription = "${movie.title} Poster image"
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = movie.title
        )
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = movie.overview
        )
    }
}
