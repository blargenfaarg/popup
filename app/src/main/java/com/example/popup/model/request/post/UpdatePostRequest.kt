package com.example.popup.model.request.post

import com.example.popup.model.domain.common.Location
import com.example.popup.model.domain.common.PostType
import com.example.popup.model.domain.common.UTCTime

/**
 * Represents the request object used to update a post
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class UpdatePostRequest(
    val userId: Long,
    val postId: Long,
    val title: String? = null,
    val description: String? = null,
    val location: Location? = null,
    val type: PostType? = null,
    val deletedImages: MutableList<Long>? = null,
    val endTime: UTCTime? = null,
    val startTime: UTCTime? = null
)
