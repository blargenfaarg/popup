package com.example.popup.model.request.post

import com.example.popup.model.domain.common.Location

/**
 * Represents the request object used to find posts
 *
 * @author Benjamin Michael
 * Project: KotlinApi
 * Created on: 9/20/2024
 */
data class FindPostsRequest(
    val userId: Long,
    val location: Location,
    val searchRange: Double
)
