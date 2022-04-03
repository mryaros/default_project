package com.psu.econom.aspirant.default_project.user_auth.dao

import com.psu.econom.aspirant.default_project.user_auth.entity.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface TokenDao : JpaRepository<Token, String> {
    fun findByToken(token: String): Token?

    fun findByRefreshToken(token: String): Token?

    @Transactional
    fun deleteAllByUserId(userId: String)
}