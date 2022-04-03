package com.psu.econom.aspirant.default_project.user_auth.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.psu.econom.aspirant.default_project.user_auth.entity.User
import io.jsonwebtoken.*
import io.jsonwebtoken.impl.DefaultClaims
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtUtils {
    @Value("\${jwt.accessTokenSecret}")
    private lateinit var accessTokenSecret: String

    @Value("\${jwt.refreshTokenSecret}")
    private lateinit var refreshTokenSecret: String

    //TODO вынести в конфиги
    @Value("\${jwt.accessTokenExpirationTime}")
    private var accessTokenExpirationTime: Long = 0

    @Value("\${jwt.refreshTokenExpirationTime}")
    private var refreshTokenExpirationTime: Long = 0

    fun generateAccessToken(user: User, roles: List<String> = emptyList()): String {
        val mapper = ObjectMapper()
        val sub = mapper.writeValueAsString(TokenSubject(user.username, roles))

        val claims = DefaultClaims()
        claims["sub"] = sub
        return generateToken(claims, accessTokenSecret, accessTokenExpirationTime)
    }

    fun generateRefreshToken(user: User, roles: List<String> = emptyList()): String {
        val mapper = ObjectMapper()
        val sub = mapper.writeValueAsString(TokenSubject(user.username, roles))

        val claims = DefaultClaims()
        claims["sub"] = sub
        return generateToken(claims, refreshTokenSecret, refreshTokenExpirationTime)
    }

    fun getAccessTokenSubject(token: String?): TokenSubject {
        return getTokenSubject(token, accessTokenSecret)
    }

    fun getRefreshTokenSubject(token: String?): TokenSubject {
        return getTokenSubject(token, refreshTokenSecret)
    }

    fun validateAccessToken(token: String?, request: HttpServletRequest): Boolean {
        return validateToken(token, accessTokenSecret, request)
    }

    fun validateRefreshToken(token: String?, request: HttpServletRequest?): Boolean {
        return validateToken(token, refreshTokenSecret, request)
    }

    companion object {
        fun parseAuthorizationHeader(request: HttpServletRequest, prefix: String): String? {
            val header = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (!StringUtils.hasText(header) || !header.startsWith(prefix)) {
                return null
            }
            return header.split(" ")[1].trim()
        }

        private fun generateToken(claims: Claims, secretKey: String, expirationTime: Long): String {

            return Jwts.builder().setClaims(claims)
                    .setIssuedAt(Date())
                    .setExpiration(Date((Date().time + expirationTime)))
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact()
        }

        private fun getTokenSubject(token: String?, secretKey: String): TokenSubject {
            val mapper = ObjectMapper()

            return mapper.readValue(getAllClaimsFromToken(token, secretKey).subject, TokenSubject::class.java)
        }

        private fun getAllClaimsFromToken(token: String?, secretKey: String): Claims {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        }

        private fun validateToken(token: String?, signingKey: String, request: HttpServletRequest?): Boolean {
            try {
                Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token)
                return true
            } catch (e: ExpiredJwtException) {
                request?.setAttribute("expired", e.message)
                e.printStackTrace()
            } catch (e: MalformedJwtException) {
                request?.setAttribute("malformed", e.message)
                e.printStackTrace()
            } catch (e: SignatureException) {
                request?.setAttribute("signature", e.message)
                e.printStackTrace()
            } catch (e: JwtException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
            return false
        }
    }
}