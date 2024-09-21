package com.example.popup.networking.api

import com.example.popup.model.response.Error

/**
 * A response from the api containing either expected data, or error
 *
 * @author Benjamin Michael
 * Project: KotlinApi
 * Created on: 9/20/2024
 */
class ApiResponse<T>(
    val data: T? = null,
    val error: Error? = null
) {
    companion object {
        /**
         * Craft a success api response with some return data
         */
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(data = data)

        /**
         * Craft an empty success api response
         */
        fun <T> success(): ApiResponse<T> = ApiResponse()

        /**
         * Craft a failure api response with an error
         */
        fun <T> failure(error: Error): ApiResponse<T> = ApiResponse(error = error)

        /**
         * Craft a failure api response with no error or body
         */
        fun <T> failure(): ApiResponse<T> = ApiResponse()
    }

    /**
     * Check if the request was successful
     */
    fun wasSuccessful(): Boolean = error == null

    override fun toString(): String {
        return "ApiResponse(data = $data, error = $error)"
    }
}