package com.example.popup.model.domain

/**
 * Represents the backend model for a session token response
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class SessionToken(
    val username: String,
    val token: String
)
