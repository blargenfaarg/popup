package com.example.popup.model.domain

import com.example.popup.model.domain.Image
import com.example.popup.model.domain.common.PostType

/**
 * Represents the backend model for a user
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class User(
    val id: Long,
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
    val profilePicture: Image?,
    val preferences: MutableList<PostType>?
)
