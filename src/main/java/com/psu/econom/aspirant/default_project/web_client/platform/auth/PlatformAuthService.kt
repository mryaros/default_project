package com.psu.econom.aspirant.default_project.web_client.platform.auth

import com.psu.econom.aspirant.default_project.web_client.InvokerFactory
import com.psu.econom.aspirant.default_project.web_client.platform.auth.enum.EGrantType
import com.psu.econom.aspirant.default_project.web_client.platform.auth.enum.EResponseType
import com.psu.econom.aspirant.default_project.web_client.platform.auth.enum.EScope
import com.psu.econom.aspirant.default_project.web_client.platform.auth.pojo.SsoToken
import com.psu.econom.aspirant.sso_server.sso_consumer_api.oauth.IOAuthResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PlatformAuthService {
    @Value("\${prop.platform.client_id}")
    private lateinit var clientId: String

    @Value("\${prop.platform.client_secret}")
    private lateinit var clientSecret: String

    @Value("\${prop.platform.redirect_uri}")
    private lateinit var redirectUri: String

    @Autowired
    private lateinit var invoker: InvokerFactory

    fun sendAuthorizeUserRequest() {
        val invoker = invoker.getPlatformProxy(IOAuthResource::class.java)
        invoker.authorizeUser(
                responseType = EResponseType.CODE.code,
                clientId = clientId,
                redirectUri = redirectUri,
                scope = EScope.ALL.code,
                state = null
        )
    }

    fun sendAuthTokenRequest(code: String): SsoToken {
        val invoker = invoker.getPlatformProxy(IOAuthResource::class.java)
        val token = invoker.getToken(
                grantType = EGrantType.AUTHORIZATION_CODE.code,
                code = code,
                clientId = clientId,
                clientSecret = clientSecret,
                redirectUri = redirectUri,
                refreshToken = null
        )

        return SsoToken.of(token)
    }

    fun sendRefreshTokenRequest(refreshToken: String): SsoToken {
        val invoker = invoker.getPlatformProxy(IOAuthResource::class.java)
        val token = invoker.getToken(
                grantType = EGrantType.REFRESH_TOKEN.code,
                code = null,
                clientId = null,
                clientSecret = null,
                redirectUri = null,
                refreshToken = refreshToken
        )

        return SsoToken.of(token)
    }
}