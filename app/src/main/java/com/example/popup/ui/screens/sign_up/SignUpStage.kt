package com.example.popup.ui.screens.sign_up

/**
 * The different stages of the sign up process
 *
 * NOTE: THESE ARE IN ORDER - do not change the order of the enum values
 *
 * @author Benjamin Michael
 * Project: Pop-Up
 * Created on: 9/24/2024
 */
enum class SignUpStage {
    GET_STARTED,
    VERIFY_EMAIL,
    PREFERENCES,
    PERSONAL_INFORMATION;

    companion object {
        fun showBackArrow(stage: SignUpStage): Boolean {
            return when (stage) {
                GET_STARTED -> false
                PREFERENCES -> false
                VERIFY_EMAIL -> true
                PERSONAL_INFORMATION -> true
            }
        }

        fun showNextArrow(stage: SignUpStage): Boolean {
            return when (stage) {
                GET_STARTED -> false
                PREFERENCES -> true
                VERIFY_EMAIL -> false
                PERSONAL_INFORMATION -> false
            }
        }
    }
}