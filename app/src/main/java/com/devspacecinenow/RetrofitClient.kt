
package com.devspacecinenow

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/movie/"

    private val httpClient: OkHttpClient
        get() {
            val clientBuilder = OkHttpClient.Builder()
            val token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0ZGZiNTUxYzlkZGY3Yjc3NmQzZDliNTg0NThmZmY3ZSIsIm5iZiI6MTczOTIxMDE5Ny4yMjEsInN1YiI6IjY3YWEzZGQ1NGI4YjM3YjM1NTM5ZThkNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.mqXHthsIYZkADYZleQWMvxhn-Ozh8YB6E93RHg17g6w"

            clientBuilder.addInterceptor { chain ->
                val original: Request = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }

            return clientBuilder.build()
        }

    val retrofitInstance: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
}
