package com.psu.econom.aspirant.default_project.user_auth.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.psu.econom.aspirant.default_project.application.dto.ErrorResponse
import com.psu.econom.aspirant.default_project.user_auth.exception.error.ETokenError
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthExceptionHandler : AuthenticationEntryPoint {

    @Throws(IOException::class, ServletException::class)
    override fun commence(req: HttpServletRequest, resp: HttpServletResponse, ex: AuthenticationException) {
        val response = when {
            req.getAttribute("expired") != null -> ErrorResponse(
                    ETokenError.EXPIRED_TOKEN_ERROR.code,
                    ETokenError.EXPIRED_TOKEN_ERROR.message
            )
            req.getAttribute("malformed") != null -> ErrorResponse(
                    ETokenError.MALFORMED_TOKEN_ERROR.code,
                    ETokenError.MALFORMED_TOKEN_ERROR.message
            )
            req.getAttribute("signature") != null -> ErrorResponse(
                    ETokenError.INVALID_TOKEN_ERROR.code,
                    ETokenError.INVALID_TOKEN_ERROR.message
            )
            else -> ErrorResponse(ETokenError.DEFAULT_TOKEN_ERROR.code, ETokenError.DEFAULT_TOKEN_ERROR.message)
        }
        val out = resp.outputStream
        resp.status = HttpServletResponse.SC_UNAUTHORIZED
        val mapper = ObjectMapper()
        mapper.writeValue(out, response)
        out.flush()
    }
}

