package com.example.popup.networking.api

import com.example.popup.networking.http.HttpFile
import com.example.popup.networking.http.HttpResponse
import com.example.popup.networking.http.IHttpRequestBuilder
import com.example.popup.networking.http.OkHttpRequestBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.example.popup.model.domain.Post
import com.example.popup.model.domain.SessionToken
import com.example.popup.model.domain.User
import com.example.popup.model.request.post.CreatePostRequest
import com.example.popup.model.request.post.FindPostsRequest
import com.example.popup.model.request.post.GetPostsRequest
import com.example.popup.model.request.post.UpdatePostRequest
import com.example.popup.model.request.user.CreateUserRequest
import com.example.popup.model.request.user.LoginUserRequest
import com.example.popup.model.request.user.UpdateUserRequest
import com.example.popup.model.response.Error
import com.example.popup.model.response.SearchResult
import java.io.File

/**
 * Implementation of IApiService. This is also a bean that can be injected into other classes.
 * Should be used as the source of truth when contacting the API
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
class ApiService(
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
): IApiService {
    /**
     * JWT token - automatically cached after login response
     */
    private lateinit var sessionToken: String

    /**
     * Parse an HTTP response and return an api response from it
     *
     * @param httpResponse the response from the server
     * @return an api response denoting success/failure with body
     */
    private inline fun <reified T> buildApiResponse(httpResponse: HttpResponse): ApiResponse<T> {
        return try {
            if (httpResponse.isSuccessful()) {
                val data = objectMapper.readValue<T>(httpResponse.responseBody)
                ApiResponse.success(data)
            } else {
                val error = objectMapper.readValue<Error>(httpResponse.responseBody)
                ApiResponse(error = error)
            }
        } catch (e: Exception) {
            ApiResponse.failure()
        }
    }

    /**
     * Parse an HTTP response and return an api response from it
     *
     * @param httpResponse the response from the server
     * @return an api response denoting success/failure
     */
    private fun buildEmptyApiResponse(httpResponse: HttpResponse): ApiResponse<Void> {
        return try {
            if (httpResponse.isSuccessful()) {
                ApiResponse.success()
            } else {
                val error = objectMapper.readValue<Error>(httpResponse.responseBody)
                ApiResponse.failure(error = error)
            }
        } catch(e: Exception) {
            ApiResponse.failure()
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    //                                  API calls for users
    // -----------------------------------------------------------------------------------------------------------------

    override suspend fun createUser(request: CreateUserRequest, image: File?): ApiResponse<User> {
        val rb = OkHttpRequestBuilder.builder().post(url = ApiRoutes.USERS_CREATE)
            .header("Content-Type", "multipart/form-data")
            .file(file = HttpFile.fromJson(objectMapper.writeValueAsString(request)))

        if (image != null) {
            rb.file(file = HttpFile.fromFile(image, "image"))
        }

        val response = rb.send()
        println("Server response from http call -> $response")
        return buildApiResponse<User>(response)
    }

    override suspend fun loginUser(request: LoginUserRequest): ApiResponse<SessionToken> {
        val httpResponse = OkHttpRequestBuilder.builder().post(url = ApiRoutes.USERS_LOGIN)
            .json(request)
            .send()

        val apiResponse = buildApiResponse<SessionToken>(httpResponse)

        if (apiResponse.wasSuccessful()) {
            println("Response from server = $apiResponse")
            // Eh, should be safe... right?
            this.sessionToken = apiResponse.data!!.token
        }

        return apiResponse
    }

    override suspend fun updateUser(request: UpdateUserRequest, image: File?): ApiResponse<User> {
        val rb = OkHttpRequestBuilder.builder().put(url = ApiRoutes.USERS_UPDATE)
            .header("Authorization", "Bearer $sessionToken")
            .json(request)

        if (image != null) {
            rb.file(file = HttpFile.fromFile(image, "image"))
        }

        val response = rb.send()
        return buildApiResponse<User>(response)
    }

    override suspend fun deleteUser(userId: Long): ApiResponse<Void> {
        val response = OkHttpRequestBuilder.builder().delete(url = ApiRoutes.USERS_DELETE)
            .header("Authorization", "Bearer $sessionToken")
            .param("id", userId.toString())
            .send()

        return buildEmptyApiResponse(response)
    }

    override suspend fun getUser(userId: Long): ApiResponse<User> {
        val response = OkHttpRequestBuilder.builder().get(url = ApiRoutes.USERS_GET)
            .header("Authorization", "Bearer $sessionToken")
            .param("id", userId.toString())
            .send()

        return buildApiResponse<User>(response)
    }

    // -----------------------------------------------------------------------------------------------------------------
    //                                  API calls for posts
    // -----------------------------------------------------------------------------------------------------------------

    override suspend fun createPost(request: CreatePostRequest, images: MutableList<File>?): ApiResponse<Post> {
        val rb = OkHttpRequestBuilder.builder().post(url = ApiRoutes.POSTS_CREATE)
            .header("Authorization", "Bearer $sessionToken")
            .file(file = HttpFile.fromJson(objectMapper.writeValueAsString(request)))

        images?.forEach { image ->
            rb.file(file = HttpFile.fromFile(image, "images"))
        }

        val response = rb.send()
        return buildApiResponse<Post>(response)
    }

    override suspend fun getPosts(request: GetPostsRequest): ApiResponse<Array<Post>> {
        val response = OkHttpRequestBuilder.builder().get(url = ApiRoutes.POSTS_GET)
            .header("Authorization", "Bearer $sessionToken")
            .json(request)
            .send()

        return buildApiResponse<Array<Post>>(response)
    }

    override suspend fun updatePost(request: UpdatePostRequest, images: MutableList<File>?): ApiResponse<Post> {
        val rb = OkHttpRequestBuilder.builder().put(url = ApiRoutes.POSTS_UPDATE)
            .header("Authorization", "Bearer $sessionToken")
            .file(file = HttpFile.fromJson(objectMapper.writeValueAsString(request)))

        images?.forEach { image ->
            rb.file(file = HttpFile.fromFile(image, "images"))
        }

        val response = rb.send()
        return buildApiResponse<Post>(response)
    }

    override suspend fun deletePost(postId: Long, userId: Long): ApiResponse<Void> {
        val response = OkHttpRequestBuilder.builder().delete(url = ApiRoutes.POSTS_DELETE)
            .header("Authorization", "Bearer $sessionToken")
            .param("postId", postId.toString())
            .param("userId", userId.toString())
            .send()

        return buildEmptyApiResponse(response)
    }

    override suspend fun findPosts(request: FindPostsRequest): ApiResponse<SearchResult> {
        val response = OkHttpRequestBuilder.builder().get(url = ApiRoutes.POSTS_SEARCH)
            .header("Authorization", "Bearer $sessionToken")
            .json(request)
            .send()

        return buildApiResponse<SearchResult>(response)
    }

    override suspend fun nextPagination(paginationId: Long): ApiResponse<SearchResult> {
        val response = OkHttpRequestBuilder.builder().get(url = "${ApiRoutes.POSTS_SEARCH}/$paginationId")
            .header("Authorization", "Bearer $sessionToken")
            .send()

        return buildApiResponse<SearchResult>(response)
    }

}