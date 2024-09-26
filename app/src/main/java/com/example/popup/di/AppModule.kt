package com.example.popup.di

import com.example.popup.networking.api.ApiService
import com.example.popup.networking.api.IApiService
import com.example.popup.ui.screens.login.NavigationHandler
import com.example.popup.ui.screens.sign_up.SignUpCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Defines the dependencies for the project.
 * Any dependencies in this module are application scoped, and their lifecycle will be that of the
 * app.
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/22/2024
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesApiService(): IApiService = ApiService()

    @Provides
    @Singleton
    fun provideNavigationHandler(): NavigationHandler = NavigationHandler()

    @Provides
    @Singleton
    fun provideSignUpCache(): SignUpCache = SignUpCache()
}