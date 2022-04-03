package com.psu.econom.aspirant.default_project.web_client

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * Запросы к другим системам должны быть описаны интерфейсом.
 * При вызове метода из интерфейса мы на самом деле будем обращаться к прослойке, которая будет вызывать запрос к внешней системе
 * Это абстрактный класс для таких прослоек, в котором есть базовая реализация.
 * В случае появления новых систем, к которым надо отправлятть http запросы по REST необходимо добавить его наследника
 **/
@Component
abstract class ABaseRequestProxy : InvocationHandler {

    abstract var baseUrl: String

    abstract var connectionTimeout: Int

    private val webClient: WebClient by lazy {
        val httpClient =
                HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
                        .responseTimeout(Duration.ofMillis(connectionTimeout.toLong()))
                        .doOnConnected { conn ->
                            conn.addHandlerLast(ReadTimeoutHandler(connectionTimeout.toLong(), TimeUnit.MILLISECONDS))
                            conn.addHandlerLast(WriteTimeoutHandler(connectionTimeout.toLong(), TimeUnit.MILLISECONDS))
                        }

        return@lazy WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(ReactorClientHttpConnector(httpClient))
                .build()
    }

    protected open fun getHeadersMap(headerParamMap: Map<String, String?>): LinkedMultiValueMap<String, String> {
        val map = LinkedMultiValueMap<String, String>()
        headerParamMap.forEach { (header, value) -> map[header] = value }

        return map
    }

    //TODO добавить метод, который выполнять перед отправкой запроса. В нем должна быть доп логика, например проверка токена и его refresh при необходимости
    override fun invoke(proxy: Any?, method: Method, args: Array<Any?>?): Any? {
        return run(method, args)
    }

    private fun run(method: Method, args: Array<Any?>?): Any? {
        var uri = ""
        if (method.declaringClass.isAnnotationPresent(RequestMapping::class.java)) {
            uri += method.declaringClass.getAnnotation(RequestMapping::class.java).value[0]
        }

        var httpMethod = HttpMethod.GET
        if (method.isAnnotationPresent(GetMapping::class.java)) {
            httpMethod = HttpMethod.GET
            if (method.getAnnotation(GetMapping::class.java).value.isNotEmpty()) {
                uri += "/${method.getAnnotation(GetMapping::class.java).value[0]}"
            }
        }
        if (method.isAnnotationPresent(PostMapping::class.java)) {
            httpMethod = HttpMethod.POST
            if (method.getAnnotation(PostMapping::class.java).value.isNotEmpty()) {
                uri += "/${method.getAnnotation(PostMapping::class.java).value[0]}"
            }
        }
        if (method.isAnnotationPresent(PutMapping::class.java)) {
            httpMethod = HttpMethod.PUT
            if (method.getAnnotation(PutMapping::class.java).value.isNotEmpty()) {
                uri += "/${method.getAnnotation(PutMapping::class.java).value[0]}"
            }
        }
        if (method.isAnnotationPresent(DeleteMapping::class.java)) {
            httpMethod = HttpMethod.DELETE
            if (method.getAnnotation(DeleteMapping::class.java).value.isNotEmpty()) {
                uri += "/${method.getAnnotation(DeleteMapping::class.java).value[0]}"
            }
        }

        uri = uri.replace("//", "/")

        val pathParamMap = mutableMapOf<String, Any?>()
        val requestParamMap = mutableMapOf<String, Any?>()
        val headerParamMap = mutableMapOf<String, String?>()
        var body: Any? = null

        method.parameters.forEachIndexed { index, it ->
            run {
                if (it.isAnnotationPresent(PathVariable::class.java)) {
                    pathParamMap[it.getAnnotation(PathVariable::class.java).value] = args?.get(index)
                }
                if (it.isAnnotationPresent(RequestParam::class.java)) {
                    val value = args?.get(index)
                    if (value != null) {
                        requestParamMap[it.getAnnotation(RequestParam::class.java).value] = value
                    }
                }
                if (it.isAnnotationPresent(RequestBody::class.java)) {
                    body = args?.get(index)
                }
                if (it.isAnnotationPresent(RequestHeader::class.java)) {
                    headerParamMap[it.getAnnotation(RequestHeader::class.java).value] = args?.get(index)?.toString()
                }
            }
        }

        uri = preparePathParam(uri, pathParamMap)
        uri = prepareRequestParam(uri, requestParamMap)

        val headers = webClient
                .method(httpMethod)
                .uri(uri)
                .headers { it.addAll(getHeadersMap(headerParamMap)) }
        return if (body != null) {
            headers
                    .bodyValue(body!!)
                    .retrieve()
                    .bodyToMono(method.returnType) // todo вынести в абстрактный метод
                    .block()
        } else {
            headers
                    .retrieve()
                    .bodyToMono(method.returnType) // todo вынести в абстрактный метод
                    .block()
        }
    }

    companion object {
        fun preparePathParam(uri: String, paramMap: Map<String, Any?>): String {
            var prepareUri = uri
            paramMap.forEach { (name, param) -> prepareUri = prepareUri.replace("{$name}", param.toString()) }

            return prepareUri
        }

        fun prepareRequestParam(uri: String, paramMap: Map<String, Any?>): String {
            var prepareUri = uri
            if (!uri.contains("?")) {
                prepareUri += "?"
            }

            paramMap.forEach { (name, param) -> prepareUri += "$name=${param.toString()}&" }

            prepareUri = prepareUri.substring(0, prepareUri.length - 1)
            return prepareUri
        }
    }
}