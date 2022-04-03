package com.psu.econom.aspirant.default_project.user_auth.exception.error

import com.psu.econom.aspirant.default_project.application.exception.error.IError
import org.springframework.http.HttpStatus

enum class EAuthError(
        override val code: String,
        override val message: String,
        override val httpStatus: HttpStatus,
) : IError {
    DEFAULT_AUTH_ERROR("DefaultAuthError", "An error occurred during the authorization process", HttpStatus.UNAUTHORIZED)
}