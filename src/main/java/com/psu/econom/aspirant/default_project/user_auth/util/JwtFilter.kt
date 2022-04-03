package com.psu.econom.aspirant.default_project.user_auth.util

import com.psu.econom.aspirant.default_project.user_auth.service.TokenService
import com.psu.econom.aspirant.default_project.user_auth.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tokenService: TokenService

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
            request: HttpServletRequest, response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val token = JwtUtils.parseAuthorizationHeader(request, "Bearer ")
        val tokenExist = try {
            token != null && tokenService.findByToken(token) != null
        } catch (e: Exception) {
            false
        }

        //TODO проверка токена на платформе
        if (tokenExist && jwtUtils.validateAccessToken(token, request)) {
            val tokenSubject = jwtUtils.getAccessTokenSubject(token)
            val username = tokenSubject.username
            val userDetails = userService.loadUserByUsername(username)

            val authorities = userDetails.authorities

            val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities
            )
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}

