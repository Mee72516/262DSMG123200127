package com.example.bookshelf.model

data class QueryResponse(
    val items: List<BookItem>?
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo?
)

data class BookDetailResponse(
    val volumeInfo: VolumeInfo?
)

data class VolumeInfo(
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String?
)