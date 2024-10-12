package com.example.popup.mock

import com.example.popup.model.domain.Post
import com.example.popup.model.domain.SessionToken
import com.example.popup.model.domain.User
import com.example.popup.model.domain.common.UTCTime
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
import com.example.popup.networking.api.ApiResponse
import com.example.popup.networking.api.IApiService
import java.io.File

/**
 * A mock implementation of the IApiService.
 * ONLY USE FOR PREVIEWS AND/OR TESTING
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
class MockApiService: IApiService {

    /**
     * Cached data we can use during the mocking process
     */
    private lateinit var cachedUser: User
    private var cachedPosts = mutableListOf<Post>()

    override suspend fun createUser(request: CreateUserRequest, image: File?): ApiResponse<User> {
        val user = User(
            id = 1,
            firstname = request.firstname,
            lastname = request.lastname,
            username = request.username,
            email = request.email,
            preferences = request.preferences
        )
        cachedUser = user

        return ApiResponse.success(data = user)
    }

    override suspend fun validateUserCreationParams(request: CreateUserValidateRequest): ApiResponse<Void> {
        return ApiResponse.success()
    }

    override suspend fun loginUser(request: LoginUserRequest): ApiResponse<SessionToken> {
        return ApiResponse.success(
            data = SessionToken(
                username = request.username,
                token = "TEST_TOKEN"
            )
        )
    }

    override suspend fun updateUser(request: UpdateUserRequest, image: File?): ApiResponse<User> {
        return ApiResponse.success(
            data = User(
                id = 1,
                firstname = request.firstname ?: cachedUser.firstname,
                lastname = request.lastname ?: cachedUser.firstname,
                username = request.username ?: cachedUser.firstname,
                email = request.email ?: cachedUser.firstname,
                preferences = when (request.preferences) {
                    null -> cachedUser.preferences
                    else -> request.preferences
                }
            )
        )
    }

    override suspend fun deleteUser(userId: Long): ApiResponse<Void> {
        return ApiResponse.success()
    }

    override suspend fun getUser(userId: Long): ApiResponse<User> {
        if (!::cachedUser.isInitialized) {
            throw RuntimeException("Cached user is not present - have no mock data to return")
        }

        return ApiResponse.success(data = cachedUser)
    }

    override suspend fun createPost(
        request: CreatePostRequest,
        images: MutableList<File>?
    ): ApiResponse<Post> {
        return ApiResponse.success(
            data = Post(
                id = (cachedPosts.maxOfOrNull { it.id } ?: 0) + 1,
                title = request.title,
                description = request.description,
                location = request.location,
                type = request.type,
                distance = null,
                pictures = null,
                owner = cachedUser,
                startTime = request.startTime,
                endTime = request.endTime,
                postTime = UTCTime.now()
            )
        )
    }

    override suspend fun getPosts(id: Long): ApiResponse<Array<Post>> {
        return ApiResponse.success(data = cachedPosts.toTypedArray())
    }

    override suspend fun updatePost(
        request: UpdatePostRequest,
        images: MutableList<File>?
    ): ApiResponse<Post> {
        val existingPostIndex = cachedPosts.indexOfFirst { it.id == request.postId}
        if (existingPostIndex == -1) {
            throw RuntimeException("Could not find mock post with id ${request.postId}")
        }

        val existingPost = cachedPosts[existingPostIndex]
        val updatedPost = Post(
            id = request.postId,
            title = request.title ?: existingPost.title,
            description = request.description,
            location = request.location ?: existingPost.location,
            type = request.type ?: existingPost.type,
            distance = null,
            pictures = null,
            owner = cachedUser,
            startTime = request.startTime ?: existingPost.startTime,
            endTime = request.endTime ?: existingPost.endTime,
            postTime = existingPost.postTime
        )

        cachedPosts[existingPostIndex] = updatedPost

        return ApiResponse.success(data = updatedPost)
    }

    override suspend fun deletePost(postId: Long, userId: Long): ApiResponse<Void> {
        val removed = cachedPosts.removeIf { it.id == postId && it.owner.id == userId }

        return if (removed) ApiResponse.success() else ApiResponse.failure()
    }

    override suspend fun findPostListings(request: FindPostsRequest): ApiResponse<Array<Post>> {
        return ApiResponse.success(data = cachedPosts.toTypedArray())
    }

    override suspend fun getPostMapData(request: GetMapDataRequest): ApiResponse<Array<PostMapData>> {
        return ApiResponse.failure()
    }

    override suspend fun generateOtpCode(email: String): ApiResponse<Void> {
        return ApiResponse.success()
    }

    override suspend fun verifyOtpCode(request: OtpVerifyRequest): ApiResponse<Void> {
        return ApiResponse.success()
    }
}