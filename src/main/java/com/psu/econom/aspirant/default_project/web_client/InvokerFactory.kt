package com.psu.econom.aspirant.default_project.web_client

import com.psu.econom.aspirant.default_project.web_client.platform.PlatformProxy
import com.psu.econom.aspirant.default_project.web_client.platform.auth.pojo.SsoToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.reflect.Proxy

@Component
class InvokerFactory {
    @Autowired
    private lateinit var platformProxy: PlatformProxy

    fun <T> getPlatformProxy(cls: Class<T>, token: SsoToken? = null): T {
        platformProxy.token = token
        return Proxy.newProxyInstance(
                cls.classLoader, arrayOf(cls),
                platformProxy) as T
    }
}