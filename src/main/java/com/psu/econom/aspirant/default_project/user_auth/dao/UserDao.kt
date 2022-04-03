package com.psu.econom.aspirant.default_project.user_auth.dao

import com.psu.econom.aspirant.default_project.user_auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDao : JpaRepository<User, String> {
    fun findByUsername(username: String): User?
}