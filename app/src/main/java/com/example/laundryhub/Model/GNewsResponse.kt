package com.example.laundryhub.Model

data class GNewsResponse(
    val totalArticles: Int,
    val articles: List<Article>
)

data class Article(
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val source: Source
)

data class Source(
    val name: String,
    val url: String
)
