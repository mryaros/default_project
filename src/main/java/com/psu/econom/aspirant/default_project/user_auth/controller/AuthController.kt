package com.psu.econom.aspirant.default_project.user_auth.controller

import com.psu.econom.aspirant.default_project.application.exception.AException
import com.psu.econom.aspirant.default_project.user_auth.exception.error.EAuthError
import com.psu.econom.aspirant.default_project.user_auth.service.AuthService
import com.psu.econom.aspirant.default_project.web_client.platform.auth.PlatformAuthService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/auth")
@Api("auth")
open class AuthController {
    @Value("\${front.url}")
    private lateinit var frontUrl: String

    @Autowired
    private lateinit var platformAuthService: PlatformAuthService

    @Autowired
    private lateinit var authService: AuthService

    //TODO он тут не нужен. Отправлять этот запрос с фронта
    @GetMapping("login")
    @ApiOperation("Логин")
    open fun sendAuthorizeUserRequest(request: HttpServletRequest) {
        platformAuthService.sendAuthorizeUserRequest()
    }

    @GetMapping("/token")
    open fun getToken(
            @ApiParam(value = "Сгенерированный код на платформе", required = true)
            @RequestParam("code")
            code: String?,
            @ApiParam(value = "State, который мы отправили на платформу", required = true)
            @RequestParam("state")
            state: String?,
            @ApiParam(value = "Код ошибки в случае ее возникновния", required = true)
            @RequestParam("error")
            error: String?,
    ): ResponseEntity<String> {
        var redirect = frontUrl
        redirect += try {
            val token = authService.authUserByCode(code!!)
            "?token=${token.accessToken}"
        } catch (e: AException) {
            "?error=${e.error.code}"
        } catch (e: Exception) {
            "?error=${EAuthError.DEFAULT_AUTH_ERROR.code}"
        }

        val headers = HttpHeaders()
        headers.add(HttpHeaders.LOCATION, redirect)
        return ResponseEntity(headers, HttpStatus.FOUND)
    }

    //TODO нужен refresh token request, get TokenDto by token
}