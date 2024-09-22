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
 * Service that offers an easy way to interact with the api. Takes in the required parameter to send the http call,
 * handles the creation and sending of the request, then attempt to parse into the expected response body and returns
 * a single api response
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
class ApiService(
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val httpRequestBuilder: IHttpRequestBuilder = OkHttpRequestBuilder()
) {
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

    /**
     * Send a create user request to the api
     *
     * @param request the create user request containing the information to create the user
     * @param image the profile picture for the user account
     * @return the api response containing the created user account, or an error response
     */
    suspend fun createUser(request: CreateUserRequest, image: File? = null): ApiResponse<User> {
        val rb = httpRequestBuilder.post(url = ApiRoutes.USERS_CREATE)
            .header("Content-Type", "multipart/form-data")
            .file(file = HttpFile.fromJson(objectMapper.writeValueAsString(request)))

        if (image != null) {
            rb.file(file = HttpFile.fromFile(image, "image"))
        }

        val response = rb.send()
        println("Server response from http call -> $response")
        return buildApiResponse<User>(response)
    }

    /**
     * Send a login user request to the api
     *
     * @param request the login user request body
     * @return the api response containing the response, or an error response
     */
    suspend fun loginUser(request: LoginUserRequest): ApiResponse<SessionToken> {
        val httpResponse = httpRequestBuilder.post(url = ApiRoutes.USERS_LOGIN)
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

    /**
     * Send an update user request to the api
     *
     * @param request the update user request containing the new account information
     * @param image the updated profile picture for the user account
     * @return the api response containing the updated user account, or an error response
     */
    suspend fun updateUser(request: UpdateUserRequest, image: File? = null): ApiResponse<User> {
        val rb = httpRequestBuilder.put(url = ApiRoutes.USERS_UPDATE)
            .header("Authorization", "Bearer $sessionToken")
            .json(request)

        if (image != null) {
            rb.file(file = HttpFile.fromFile(image, "image"))
        }

        val response = rb.send()
        return buildApiResponse<User>(response)
    }

    /**
     * Send a delete user request to the api
     *
     * @param userId the id of the user to be deleted
     * @return successful api response if account deleted, otherwise an error response
     */
    suspend fun deleteUser(userId: Long): ApiResponse<Void> {
        val response = httpRequestBuilder.delete(url = ApiRoutes.USERS_DELETE)
            .header("Authorization", "Bearer $sessionToken")
            .param("id", userId.toString())
            .send()

        return buildEmptyApiResponse(response)
    }

    /**
     * Send a get user request to the api
     *
     * @param userId the id of the user account to get
     * @return the api response containing the retrieved user account, or an error response
     */
    suspend fun getUser(userId: Long): ApiResponse<User> {
        val response = httpRequestBuilder.get(url = ApiRoutes.USERS_GET)
            .header("Authorization", "Bearer $sessionToken")
            .param("id", userId.toString())
            .send()

        return buildApiResponse<User>(response)
    }

    // -----------------------------------------------------------------------------------------------------------------
    //                                  API calls for posts
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Send a create post request to the api
     *
     * @param request the create post request containing needed information
     * @param images list of images to be uploaded along with the post
     * @return api response containing either the Post object created, or an error response
     */
    suspend fun createPost(request: CreatePostRequest, images: MutableList<File>? = null): ApiResponse<Post> {
        val rb = httpRequestBuilder.post(url = ApiRoutes.POSTS_CREATE)
            .header("Authorization", "Bearer $sessionToken")
            .file(file = HttpFile.fromJson(objectMapper.writeValueAsString(request)))

        images?.forEach { image ->
            rb.file(file = HttpFile.fromFile(image, "images"))
        }

        val response = rb.send()
        return buildApiResponse<Post>(response)
    }

    /**
     * Send a get posts request to the api
     *
     * @param request the get posts request to send
     * @return api response containing either the list of posts found, or an error response
     */
    suspend fun getPosts(request: GetPostsRequest): ApiResponse<Array<Post>> {
        val response = httpRequestBuilder.get(url = ApiRoutes.POSTS_GET)
            .header("Authorization", "Bearer $sessionToken")
            .json(request)
            .send()

        return buildApiResponse<Array<Post>>(response)
    }

    /**
     * Send an update post request to the api
     *
     * @param request the update post request with the updated information
     * @param images list of images to be uploaded along with the post
     * @return api response containing either the Post object updated, or an error response
     */
    suspend fun updatePost(request: UpdatePostRequest, images: MutableList<File>? = null): ApiResponse<Post> {
        val rb = httpRequestBuilder.put(url = ApiRoutes.POSTS_UPDATE)
            .header("Authorization", "Bearer $sessionToken")
            .file(file = HttpFile.fromJson(objectMapper.writeValueAsString(request)))

        images?.forEach { image ->
            rb.file(file = HttpFile.fromFile(image, "images"))
        }

        val response = rb.send()
        return buildApiResponse<Post>(response)
    }

    /**
     * Send a create post request to the api
     *
     * @param postId the id of the post to delete
     * @param userId the id of the user deleting the post
     * @return api response successful if deletion went through, if not an error response
     */
    suspend fun deletePost(postId: Long, userId: Long): ApiResponse<Void> {
        val response = httpRequestBuilder.delete(url = ApiRoutes.POSTS_DELETE)
            .header("Authorization", "Bearer $sessionToken")
            .param("postId", postId.toString())
            .param("userId", userId.toString())
            .send()

        return buildEmptyApiResponse(response)
    }

    /**
     * Send a find posts request to the api
     *
     * @param request the find post request body
     * @return api response containing either the Posts found, or an error response
     */
    suspend fun findPosts(request: FindPostsRequest): ApiResponse<SearchResult> {
        val response = httpRequestBuilder.get(url = ApiRoutes.POSTS_SEARCH)
            .header("Authorization", "Bearer $sessionToken")
            .json(request)
            .send()

        return buildApiResponse<SearchResult>(response)
    }

    /**
     * Send a request to the api to load the next pagination from a search
     *
     * @param paginationId the id of the pagination we want to load
     * @return api response containing either the nest Posts found, or an error response
     */
    suspend fun nextPagination(paginationId: Long): ApiResponse<SearchResult> {
        val response = httpRequestBuilder.get(url = "${ApiRoutes.POSTS_SEARCH}/$paginationId")
            .header("Authorization", "Bearer $sessionToken")
            .send()

        return buildApiResponse<SearchResult>(response)
    }

}