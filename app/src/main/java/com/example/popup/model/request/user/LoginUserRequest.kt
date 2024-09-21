package com.example.popup.model.request.user

/**
 * Represents the request object used to create an account
 *
 * @author Benjamin Michael
 * Project: KotlinApi
 * Created on: 9/20/2024
 */
data class LoginUserRequest(
    val username: String,
    val password: String
)
