package com.devspacecinenow


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _nowPlayingMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val nowPlayingMovies: StateFlow<List<MovieDto>> get() = _nowPlayingMovies

    private val _popularMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val popularMovies: StateFlow<List<MovieDto>> get() = _popularMovies

    private val _upcomingMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val upcomingMovies: StateFlow<List<MovieDto>> get() = _upcomingMovies

    private val _topRatedMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val topRatedMovies: StateFlow<List<MovieDto>> get() = _topRatedMovies

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)

        _isLoading.value = true

        val movieCategories = mapOf(
            apiService.getNowPlayingMovies() to _nowPlayingMovies,
            apiService.getPopularMovies() to _popularMovies,
            apiService.getUpcomingMovies() to _upcomingMovies,
            apiService.getTopRatedMovies() to _topRatedMovies
        )

        var completedRequests = 0
        val totalRequests = movieCategories.size

        for ((call, stateFlow) in movieCategories) {
            call.enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    if (response.isSuccessful) {
                        stateFlow.value = response.body()?.results ?: emptyList()
                    }
                    completedRequests++
                    if (completedRequests == totalRequests) {
                        _isLoading.value = false
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    completedRequests++
                    if (completedRequests == totalRequests) {
                        _isLoading.value = false
                    }
                }
            })
        }
    }
}
