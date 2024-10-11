package com.example.popup.model.response

import com.example.popup.model.domain.common.Location
import com.example.popup.model.domain.common.PostType

/**
 * Represents the response object returned from a post map data query
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class PostMapData(
    val id: Long,
    val postType: PostType,
    val location: Location
)
