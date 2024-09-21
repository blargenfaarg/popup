package com.example.popup.model.response

import com.example.popup.model.domain.Post

/**
 * Represents the response object returned when searching for posts
 *
 * @author Benjamin Michael
 * Project: KotlinApi
 * Created on: 9/20/2024
 */
data class SearchResult(
    val paginationId: Long? = null,
    val results: MutableList<Post>
)
