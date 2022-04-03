package com.psu.econom.aspirant.default_project.web_client.platform.user

import com.psu.econom.aspirant.default_project.web_client.InvokerFactory
import com.psu.econom.aspirant.default_project.web_client.platform.auth.pojo.SsoToken
import com.psu.econom.aspirant.sso_server.sso_consumer_api.user.IUserResource
import com.psu.econom.aspirant.sso_server.sso_consumer_api.user.pojo.UserResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PlatformUserService {
    @Autowired
    private lateinit var invoker: InvokerFactory

    //TODO в метод подпихивать токен
    fun getUser(id: String): UserResponse {
        val invoker = invoker.getPlatformProxy(IUserResource::class.java)
        return invoker.getUserById(id)
    }

    fun getUserByToken(token: SsoToken): UserResponse {
        val invoker = invoker.getPlatformProxy(IUserResource::class.java, token)

        return invoker.getUserByToken(token.accessToken)  //todo а если мы обновим токен в процессе?
    }
}