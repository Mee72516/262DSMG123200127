package com.example.bookshelf.data

import com.example.bookshelf.network.BooksApiService

interface BooksRepository {
    suspend fun getBookThumbnails(query: String): List<String>
}

class NetworkBooksRepository(
    private val apiService: BooksApiService
) : BooksRepository {
    override suspend fun getBookThumbnails(query: String): List<String> {
        val searchResponse = apiService.searchBooks(query)
        val thumbnails = mutableListOf<String>()

        searchResponse.items?.forEach { book ->
            book.volumeInfo?.imageLinks?.thumbnail?.let { url ->
                thumbnails.add(url.replace("http://", "https://"))
            }
        }

        return thumbnails
    }
}