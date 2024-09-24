package com.example.popup.model.request.user

import com.example.popup.model.domain.common.PostType

/**
 * Represents the request object used to create an account
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class CreateUserRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val username: String,
    val password: String,
    val preferences: MutableList<PostType>? = null
)
