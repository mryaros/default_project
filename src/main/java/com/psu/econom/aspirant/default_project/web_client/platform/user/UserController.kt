package com.psu.econom.aspirant.default_project.web_client.platform.user

import com.psu.econom.aspirant.sso_server.sso_consumer_api.user.pojo.UserResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

//TODO этот контроллер явно должен быть не тут и пользователя для фронта надо получать не из ССО
@RestController
@RequestMapping("/users")
@Api("/users")
open class UserController {

    @Autowired
    private lateinit var platformUserService: PlatformUserService

    @GetMapping("/{id}")
    @ApiOperation("Получение пользователя")
    open fun getUser(@PathVariable id: String): UserResponse? {
        return platformUserService.getUser(id)
    }
}