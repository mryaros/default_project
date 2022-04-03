package com.psu.econom.aspirant.default_project.user_auth.util

import com.psu.econom.aspirant.default_project.application.exception.ControllerException
import com.psu.econom.aspirant.default_project.application.exception.error.EGlobalError
import com.psu.econom.aspirant.default_project.user_auth.entity.User
import org.springframework.security.core.context.SecurityContextHolder

object AuthHelper {

    @Throws(ControllerException::class)
    fun validateRequestByUser(userId: String?) {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        if (user.id != userId) {
            throw ControllerException(EGlobalError.FORBIDDEN_OPERATION_ERROR)
        }
    }

    //TODO этот метод нужен?
    fun hasAuthentication(): Boolean {
        return try {
            SecurityContextHolder.getContext().authentication.principal as User
            true
        } catch (e: Exception) {
            false
        }
    }
}