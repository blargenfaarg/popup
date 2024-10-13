package com.example.popup.networking.api

import com.example.popup.model.domain.Post
import com.example.popup.model.domain.SessionToken
import com.example.popup.model.domain.User
import com.example.popup.model.request.OtpVerifyRequest
import com.example.popup.model.request.post.CreatePostRequest
import com.example.popup.model.request.post.FindPostsRequest
import com.example.popup.model.request.post.GetMapDataRequest
import com.example.popup.model.request.post.UpdatePostRequest
import com.example.popup.model.request.user.CreateUserRequest
import com.example.popup.model.request.user.CreateUserValidateRequest
import com.example.popup.model.request.user.LoginUserRequest
import com.example.popup.model.request.user.UpdateUserRequest
import com.example.popup.model.response.PostMapData
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
interface IApiService {

    /**
     * Send a create user request to the api
     *
     * @param request the create user request containing the information to create the user
     * @param image the profile picture for the user account
     * @return the api response containing the created user account, or an error response
     */
    suspend fun createUser(request: CreateUserRequest, image: File? = null): ApiResponse<User>

    /**
     * Send a post request to the API to validate a username and email, ensuring it is not taken.
     * A response of 200 means you are good
     *
     * @param request the request json
     * @return api response dictating if the validation was successful
     */
    suspend fun validateUserCreationParams(request: CreateUserValidateRequest): ApiResponse<Void>

    /**
     * Send a login user request to the api
     *
     * @param request the login user request body
     * @return the api response containing the response, or an error response
     */
    suspend fun loginUser(request: LoginUserRequest): ApiResponse<SessionToken>

    /**
     * Send an update user request to the api
     *
     * @param request the update user request containing the new account information
     * @param image the updated profile picture for the user account
     * @return the api response containing the updated user account, or an error response
     */
    suspend fun updateUser(request: UpdateUserRequest, image: File? = null): ApiResponse<User>

    /**
     * Send a delete user request to the api
     *
     * @param userId the id of the user to be deleted
     * @return successful api response if account deleted, otherwise an error response
     */
    suspend fun deleteUser(userId: Long): ApiResponse<Void>

    /**
     * Send a get user request to the api
     *
     * @param userId the id of the user account to get
     * @return the api response containing the retrieved user account, or an error response
     */
    suspend fun getUser(userId: Long): ApiResponse<User>

    /**
     * Send a create post request to the api
     *
     * @param request the create post request containing needed information
     * @param images list of images to be uploaded along with the post
     * @return api response containing either the Post object created, or an error response
     */
    suspend fun createPost(request: CreatePostRequest, images: MutableList<File>? = null): ApiResponse<Post>

    /**
     * Send a get posts request to the api
     *
     * @param id the id of the user to get the posts for
     * @return api response containing either the list of posts found, or an error response
     */
    suspend fun getPosts(id: Long): ApiResponse<Array<Post>>

    /**
     * Get the post that is identified by the provided id
     *
     * @param id the id of the post to find
     * @return api response containing either the post found, or an error response
     */
    suspend fun getPost(id: Long): ApiResponse<Post>

    /**
     * Send an update post request to the api
     *
     * @param request the update post request with the updated information
     * @param images list of images to be uploaded along with the post
     * @return api response containing either the Post object updated, or an error response
     */
    suspend fun updatePost(request: UpdatePostRequest, images: MutableList<File>? = null): ApiResponse<Post>

    /**
     * Send a create post request to the api
     *
     * @param postId the id of the post to delete
     * @param userId the id of the user deleting the post
     * @return api response successful if deletion went through, if not an error response
     */
    suspend fun deletePost(postId: Long, userId: Long): ApiResponse<Void>

    /**
     * Send a find posts request to the api
     *
     * @param request the find post request body
     * @return api response containing either the Posts found, or an error response
     */
    suspend fun findPostListings(request: FindPostsRequest): ApiResponse<Array<Post>>

    /**
     * Get the post map data for a given viewport
     *
     * @param request the viewport to find data inside
     * @return api response with the map data condensed version of posts
     */
    suspend fun getPostMapData(request: GetMapDataRequest): ApiResponse<Array<PostMapData>>

    /**
     * Send a request to generate an opt code that is sent via email.
     *
     * @param email the email to send the otp code
     * @return api response if successful or not
     */
    suspend fun generateOtpCode(email: String): ApiResponse<Void>

    /**
     * Send a request to validate an otp code.
     * Assume a good response code of 200 means that the validation was successful.
     *
     * @param request otp verify request
     * @return api response dictating the result of the call
     */
    suspend fun verifyOtpCode(request: OtpVerifyRequest): ApiResponse<Void>
}