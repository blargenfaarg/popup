package com.example.popup.model.request.user

/**
 * Represents the request object used to validate a username and email before creating an account
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class CreateUserValidateRequest(
    val username: String,
    val email: String
)
