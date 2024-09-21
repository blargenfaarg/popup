package com.example.popup.model.request.post

import com.example.popup.model.domain.common.Location
import com.example.popup.model.domain.common.PostType
import com.example.popup.model.domain.common.UTCTime

/**
 * Represents the request object used to create a post
 *
 * @author Benjamin Michael
 * Project: KotlinApi
 * Created on: 9/20/2024
 */
data class CreatePostRequest(
    val userId: Long,
    val title: String,
    val description: String? = null,
    val location: Location,
    val type: PostType,
    val startTime: UTCTime,
    val endTime: UTCTime
)
