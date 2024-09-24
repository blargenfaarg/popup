package com.example.popup.networking.http

/**
 * The response data class to be returned from the http request builder
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class HttpResponse(
    val responseCode: Int,
    val contentType: String,
    val responseBody: String
) {
    fun isSuccessful(): Boolean = responseCode in 200..299
}