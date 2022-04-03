package com.psu.econom.aspirant.default_project.user_auth.service

import com.psu.econom.aspirant.default_project.application.exception.ControllerException
import com.psu.econom.aspirant.default_project.application.exception.error.EGlobalError
import com.psu.econom.aspirant.default_project.user_auth.dao.TokenDao
import com.psu.econom.aspirant.default_project.user_auth.entity.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class TokenService {
    @Autowired
    private lateinit var tokenDao: TokenDao

    fun save(token: Token): Token = tokenDao.save(token)
    fun delete(token: Token) = tokenDao.deleteById(token.id)
    fun deleteAll(userId: String) = tokenDao.deleteAllByUserId(userId)

    fun findByToken(token: String): Token? {
        return tokenDao.findByToken(token)
    }

    @Throws(ControllerException::class)
    fun findByRefreshToken(token: String): Token {
        return tokenDao.findByRefreshToken(token) ?: throw ControllerException(EGlobalError.INVALID_REQUEST_ERROR)
    }
}