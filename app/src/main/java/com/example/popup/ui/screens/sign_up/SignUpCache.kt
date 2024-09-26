package com.example.popup.ui.screens.sign_up

import com.example.popup.model.domain.common.PostType
import com.example.popup.model.request.user.CreateUserRequest

/**
 * This is the most ghetto way to solve the issue where I want to share the sign up view model
 * between more than one view, but that goes against MVVM architecture so android is telling me
 * no
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
class SignUpCache {

    private var username = ""
    private var password = ""
    private var email = ""
    private var preferences = mutableListOf<PostType>()

    /**
     * Cache the getting started info
     */
    fun cacheGettingStartedInfo(username: String, password: String, email: String) {
        this.username = username
        this.password = password
        this.email = email
    }

    /**
     * Cache the preferences
     */
    fun cachePreferences(preferences: Collection<PostType>) {
        this.preferences.clear()
        this.preferences.addAll(preferences)
    }

    /**
     * Build the create user request with the cached data and the provided data
     */
    fun buildCreateUserRequest(
        firstname: String,
        lastname: String): CreateUserRequest {

        return CreateUserRequest(
            firstname = firstname,
            lastname = lastname,
            email = email,
            username = username,
            password = password,
            preferences = if (preferences.isEmpty()) null else preferences
        )
    }

    /**
     * Clear the cached data
     */
    fun clearCache() {
        username = ""
        password = ""
        email = ""
        preferences.clear()
    }
}