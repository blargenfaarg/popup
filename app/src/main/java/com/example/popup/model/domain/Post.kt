package com.example.popup.model.domain

import com.example.popup.model.domain.common.Location
import com.example.popup.model.domain.common.PostType
import com.example.popup.model.domain.common.UTCTime

/**
 * Represents the backend model for a post
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class Post(
    val id: Long,
    val title: String,
    val description: String? = null,
    val location: Location,
    val distance: Double? = null,
    val type: PostType,
    val pictures: List<String>? = listOf(),
    val postTime: UTCTime,
    val startTime: UTCTime,
    val endTime: UTCTime,
    val owner: User
)
