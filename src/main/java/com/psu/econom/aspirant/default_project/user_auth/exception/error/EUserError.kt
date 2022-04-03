package com.psu.econom.aspirant.default_project.user_auth.exception.error

import com.psu.econom.aspirant.default_project.application.exception.error.IError
import org.springframework.http.HttpStatus

enum class EUserError(
        override val code: String,
        override val message: String,
        override val httpStatus: HttpStatus,
) : IError {
    USER_NOT_FOUND_ERROR("UserNotFoundError", "User with this ID is not found", HttpStatus.NOT_FOUND),
    INCORRECT_PASSWORD("IncorrectPassword", "Incorrect Password", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXIST_ERROR("UserAlreadyExistError", "User with this login already exists", HttpStatus.UNAUTHORIZED)
}