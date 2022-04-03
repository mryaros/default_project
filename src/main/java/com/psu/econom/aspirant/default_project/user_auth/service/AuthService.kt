package com.psu.econom.aspirant.default_project.user_auth.service

import com.psu.econom.aspirant.default_project.application.exception.ControllerException
import com.psu.econom.aspirant.default_project.user_auth.dto.TokenDto
import com.psu.econom.aspirant.default_project.user_auth.entity.Token
import com.psu.econom.aspirant.default_project.user_auth.entity.User
import com.psu.econom.aspirant.default_project.user_auth.exception.UserException
import com.psu.econom.aspirant.default_project.user_auth.exception.error.EUserError
import com.psu.econom.aspirant.default_project.user_auth.util.JwtUtils
import com.psu.econom.aspirant.default_project.web_client.platform.auth.PlatformAuthService
import com.psu.econom.aspirant.default_project.web_client.platform.auth.pojo.SsoToken
import com.psu.econom.aspirant.default_project.web_client.platform.user.PlatformUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService {
    //TODO вынести в конфиги
    @Value("\${jwt.accessTokenExpirationTime}")
    private var accessTokenExpirationTime: Long = 0

    @Value("\${jwt.refreshTokenExpirationTime}")
    private var refreshTokenExpirationTime: Long = 0

    @Autowired
    private lateinit var platformAuthService: PlatformAuthService

    @Autowired
    private lateinit var platformUserService: PlatformUserService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var tokenService: TokenService

    fun authUserByCode(code: String): TokenDto {
        val ssoToken = platformAuthService.sendAuthTokenRequest(code)
        val platformUser = platformUserService.getUserByToken(ssoToken)

        //если пользователь у нас не сохранен, то его надо создать и записать в нашу БД
        val user = try {
            userService.getUser(platformUser.id)
        } catch (e: UserException) {
            if (e.error == EUserError.USER_NOT_FOUND_ERROR) {
                userService.addUser(platformUser)
            } else {
                throw ControllerException(e.error)
            }
        }

        val authorities = user.authorities
        val authentication = UsernamePasswordAuthenticationToken(user, null, authorities)
        SecurityContextHolder.getContext().authentication = authentication

        return generateTokens(user, ssoToken)
    }

    private fun generateTokens(user: User, ssoToken: SsoToken): TokenDto {
        //генерируем короткоживущий access token, валиден в течение 30 мин.
        val jwt = jwtUtils.generateAccessToken(user)
        //генерируем refresh token, валиден в течение 10 дней.
        val refreshJwt = jwtUtils.generateRefreshToken(user)
        //сохраняем token в БД
        val newToken = Token("", user, jwt, refreshJwt, ssoToken.ssoTokenToString())
        tokenService.save(newToken)

        return TokenDto(
                userId = user.id,
                accessToken = jwt,
                refreshToken = refreshJwt,
                expiresIn = accessTokenExpirationTime,
                refreshExpiresIn = refreshTokenExpirationTime,
        )
    }
}