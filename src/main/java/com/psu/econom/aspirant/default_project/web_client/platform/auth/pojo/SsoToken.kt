package com.psu.econom.aspirant.default_project.web_client.platform.auth.pojo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.psu.econom.aspirant.default_project.user_auth.util.JwtUtils
import com.psu.econom.aspirant.default_project.user_auth.util.TokenSubject
import com.psu.econom.aspirant.sso_server.sso_consumer_api.oauth.pojo.TokenResponse
import java.time.Duration
import java.time.LocalDateTime

data class SsoToken(
        val accessToken: String,
        @JsonSerialize(using = LocalDateTimeSerializer::class)
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        val creationDate: LocalDateTime,
        @JsonSerialize(using = DurationSerializer::class)
        @JsonDeserialize(using = DurationDeserializer::class)
        val accessTokenTTL: Duration,
        val refreshToken: String,
        @JsonSerialize(using = DurationSerializer::class)
        @JsonDeserialize(using = DurationDeserializer::class)
        val refreshTokenTTL: Duration,
) {
    private val expirationDate: LocalDateTime = creationDate.plus(accessTokenTTL)
    private val refreshExpirationDate: LocalDateTime = creationDate.plus(refreshTokenTTL)

    private fun isTokenGettingOld(refreshPeriod: Duration = this.accessTokenTTL.dividedBy(2)): Boolean {
        return LocalDateTime.now().isAfter(expirationDate.minus(refreshPeriod))
    }

    private fun isRefreshTokenGettingOld(refreshPeriod: Duration = this.refreshTokenTTL.dividedBy(2)): Boolean {
        return LocalDateTime.now().isAfter(refreshExpirationDate.minus(refreshPeriod))
    }

    @JsonIgnore
    fun isRefreshTokenExpired(): Boolean {
        return LocalDateTime.now().isAfter(refreshExpirationDate)
    }

    fun shouldRefresh(tokenPeriod: Duration = this.accessTokenTTL.dividedBy(2), refreshTokenPeriod: Duration = this.refreshTokenTTL.dividedBy(2)): Boolean {
        return isTokenGettingOld(tokenPeriod) || isRefreshTokenGettingOld(refreshTokenPeriod)
    }

    fun ssoTokenToString(): String {
        val mapper = ObjectMapper()
        mapper.findAndRegisterModules()
        return mapper.writeValueAsString(this)
    }

    companion object {
        fun of(response: TokenResponse): SsoToken {
            return SsoToken(
                    accessToken = response.accessToken,
                    creationDate = LocalDateTime.now(),
                    accessTokenTTL = Duration.ofMillis(response.expiresIn),
                    refreshToken = response.refreshToken,
                    refreshTokenTTL = Duration.ofMillis(response.refreshExpiresIn)
            )
        }

        fun getSsoTokenFromString(tokenString: String): SsoToken {
            val mapper = ObjectMapper()
            mapper.findAndRegisterModules()
            return mapper.readValue(tokenString, SsoToken::class.java)
        }
    }
}