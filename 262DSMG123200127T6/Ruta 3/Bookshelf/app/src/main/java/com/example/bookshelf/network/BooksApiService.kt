package com.example.bookshelf.network

import com.example.bookshelf.model.BookDetailResponse
import com.example.bookshelf.model.QueryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {
    @GET("volumes?key=AIzaSyD0ZQilwhEfd-L9MXGmTJ6kKj2ccrWIPDA")
    suspend fun searchBooks(@Query("q") query: String): QueryResponse

    @GET("volumes/{volumeId}?key=AIzaSyD0ZQilwhEfd-L9MXGmTJ6kKj2ccrWIPDA")
    suspend fun getBookDetails(@Path("volumeId") volumeId: String): BookDetailResponse
}