package com.example.popup.model.request

/**
 * Represents the request object used to validate an otp code
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/20/2024
 */
data class OtpVerifyRequest(
    val email: String,
    val otpCode: String
)
