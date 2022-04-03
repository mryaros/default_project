package com.psu.econom.aspirant.default_project.application.exception.error

import org.springframework.http.HttpStatus

enum class EGlobalError(
        override val code: String,
        override val message: String,
        override val httpStatus: HttpStatus,
) : IError {
    INVALID_REQUEST_ERROR("InvalidRequestError", "Invalid request. Please check request data", HttpStatus.BAD_REQUEST),
    FORBIDDEN_OPERATION_ERROR("ForbiddenOperationError", "This operation is forbidden. No access rights for current user", HttpStatus.FORBIDDEN),
    NOT_FOUND_ERROR("NotFoundError", "Requested resource not found", HttpStatus.NOT_FOUND),
    LOGIN_ERROR("LoginError", "Unauthorized request. Please check username and password", HttpStatus.UNAUTHORIZED),
    CONFLICT_REQUEST_ERROR("ConflictRequestError", "Invalid request. Server had a conflict with existing data while processing the request", HttpStatus.CONFLICT),
}