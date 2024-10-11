package com.example.popup.model.response

/**
 * Represents the response object returned when an error occurs
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class Error(
    val title: String,
    val message: String,
    val errorDetails: String? = null
)
