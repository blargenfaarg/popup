package com.example.popup.networking.api

/**
 * Http routes for the api
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/21/2024
 */
object ApiRoutes {
    private const val BASE_URL = "https://www.ciloot.lol/api"

    private const val USERS = "$BASE_URL/users"
    const val USERS_GET = USERS
    const val USERS_UPDATE = USERS
    const val USERS_DELETE = USERS
    const val USERS_LOGIN = "$USERS/login"
    const val USERS_CREATE = "$USERS/create"
    const val USERS_CREATE_VALIDATE = "$USERS_CREATE/validate"

    private const val POSTS = "$BASE_URL/posts"
    const val POSTS_CREATE = POSTS
    const val POSTS_GET = POSTS
    const val POSTS_UPDATE = POSTS
    const val POSTS_DELETE = POSTS
    const val POSTS_SEARCH = "$POSTS/listing"
    const val POSTS_MAP_DATA = "$POSTS/map-data"

    private const val OTP = "$BASE_URL/otp"
    const val OTP_GENERATE = "$OTP/generate"
    const val OTP_VERIFY = "$OTP/verify"
}