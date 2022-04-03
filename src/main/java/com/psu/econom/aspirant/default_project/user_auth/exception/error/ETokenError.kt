package com.psu.econom.aspirant.default_project.user_auth.exception.error

import com.psu.econom.aspirant.default_project.application.exception.error.IError
import org.springframework.http.HttpStatus

enum class ETokenError(
        override val code: String,
        override val message: String,
        override val httpStatus: HttpStatus,
) : IError {
    EXPIRED_TOKEN_ERROR("ExpiredTokenAuthError", "Access token is expired", HttpStatus.UNAUTHORIZED),
    MALFORMED_TOKEN_ERROR("MalformedTokenAuthError", "JWT token is not valid. Wrong format of JWT token string", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_ERROR("InvalidTokenAuthError", "Invalid JWT signature", HttpStatus.UNAUTHORIZED),
    DEFAULT_TOKEN_ERROR("DefaultTokenAuthError", "Unauthorized request. Wrong auth token", HttpStatus.UNAUTHORIZED),
}