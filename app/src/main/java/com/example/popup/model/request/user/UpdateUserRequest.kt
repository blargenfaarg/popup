package com.example.popup.model.request.user

import com.example.popup.model.domain.common.PostType

/**
 * Represents the request object used to update a user account
 *
 * @author Benjamin Michael
 * Project: KotlinApi
 * Created on: 9/20/2024
 */
data class UpdateUserRequest(
    val id: Long,
    val firstname: String? = null,
    val lastname: String? = null,
    val username: String? = null,
    val email: String? = null,
    val preferences: MutableList<PostType>? = null
)
