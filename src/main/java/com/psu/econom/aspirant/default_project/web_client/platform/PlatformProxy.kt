package com.psu.econom.aspirant.default_project.web_client.platform

import com.psu.econom.aspirant.default_project.web_client.ABaseRequestProxy
import com.psu.econom.aspirant.default_project.web_client.platform.auth.pojo.SsoToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import java.util.function.Consumer

@Component
class PlatformProxy : ABaseRequestProxy() {
    @Value("\${prop.platform.url}")
    override lateinit var baseUrl: String
    @Value("\${prop.platform.connectTimeout}")
    override var connectionTimeout: Int = 1000

    var token: SsoToken? = null

    override fun getHeadersMap(headerParamMap: Map<String, String?>): LinkedMultiValueMap<String, String> {
        val map = super.getHeadersMap(headerParamMap)
        if (token == null) {
            return map
        }

        map[HttpHeaders.AUTHORIZATION] = "Bearer ${token!!.accessToken}"

        return map
    }
}